package filelocking;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.Random;

/**
 * Consumeert waarden uit een file die fungeert als een 1-plaats buffer
 * 
 * Er wordt exclusive file locking toegepast
 * 
 * Layout bestand: 
 * Bytes 0 .. 3 :    4 bytes int with maxvalue  (De maximale waarde die geproduceerd zal worden)
 * Bytes 4 .. 7 :    4 bytes int with status    (1 betekent: nog niet gelezen door consumer)
 * Bytes 8 .. 11:    4 bytes int with value     (De waarde die geproduceerd is)
 *
 * @author marcel
 */
public class Consumer {

    private static final String BUFFERFILE = "buffer.bin";
    private static final boolean EXCLUSIVE = false;
    private static final boolean SHARED = true;
    private static final int STATUS_NOT_READ = 1;
    private static final int STATUS_READ = 0;
    private static final int NBYTES = 12;

    public static void main(String arsg[]) throws IOException, InterruptedException {

        Random r = new Random();
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
            FileChannel ch = raf.getChannel();

            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);

            boolean finished = false;
            while (!finished) {
                
                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);

                /**
                 * Try to read the data . . .
                 */

                // layout: 
                //      0 .. 3 :    4 bytes int with maxvalue
                //      4 .. 7 :    4 bytes int with status
                //      8 .. 11:    4 bytes int with value
                
                // Vraag de maximumwaarde, status en geproduceerde waarde op
                out.position(0);
                int maxVal = out.getInt();
                int status = out.getInt();
                int value = out.getInt();

                // Alleen als er iets "nieuws" geproduceerd is verwerken we de
                // gelezen value
                if (status == STATUS_NOT_READ) {
                    // Nieuwe waarde gelezen. Zet status in bestand
                    out.position(4);
                    out.putInt(STATUS_READ);
                    System.out.println("CONSUMER: " + value );
                    // Bepaal of we klaar zijn, dat is als de gelezen waarde
                    // gelijk is aan de maxVal in bytes 0 .. 3 van het bestand
                    finished = (value == maxVal);
                }

                Thread.sleep(r.nextInt(10));
                // release the lock
                exclusiveLock.release();

            }
        } catch (java.io.IOException ioe) {
            System.err.println(ioe);
        } finally {
            if (exclusiveLock != null) {
                exclusiveLock.release();
            }
        }
        System.out.println("CONSUMER: KLAAR");
    }

}
