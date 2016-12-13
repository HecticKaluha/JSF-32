package filelocking;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.Random;

/**
 * Produceert waarden die weg worden geschreven naar een file die fungeert
 * als een 1-plaats buffer
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
public class Producer {

    private static final String BUFFERFILE = "buffer.bin";
    private static final boolean EXCLUSIVE = false;
    private static final boolean SHARED = true;
    private static final int MAXVAL = 400; // We schrijven 400 waarden weg
    private static final int NBYTES = 12;

    private static final int STATUS_NOT_READ = 1;
    private static final int STATUS_READ = 0;
    
    public static void main(String arsg[]) throws IOException, InterruptedException {

        Random r = new Random();
        FileLock exclusiveLock = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(BUFFERFILE, "rw");
            FileChannel ch = raf.getChannel();
            MappedByteBuffer out = ch.map(FileChannel.MapMode.READ_WRITE, 0, NBYTES);

            int newValue = 0; // de waarde die we naar het bestand gaan schrijven
            while (newValue <= MAXVAL) {
                // Probeer het lock te verkrijgen
                exclusiveLock = ch.lock(0, NBYTES, EXCLUSIVE);

                /**
                 * Now modify the data . . .
                 */

                // layout: 
                //      0 .. 3 :    4 bytes int with maxvalue
                //      4 .. 7 :    4 bytes int with status
                //      8 .. 11:    4 bytes int with value
                
                // Vraag waarde van status op
                out.position(4);
                int status = out.getInt();

                // Alleen als de voorgaande geproduceerde waarde is gelezen
                // dwz status != STATUS_NOT_READ
                // <of> 
                // als er nog niets geproduceerd is kunnen we schrijven
                if (((status != STATUS_NOT_READ) || (newValue == 0))) {
                    // Ga naar het begin van het bestand
                    out.position(0);
                    // Schrijf maxwaarde weg
                    out.putInt(MAXVAL);
                    // Er wordt een nieuwe waarde geschreven dus deze is
                    // nog niet gelezen door de client --> zet naar correcte
                    // status
                    out.putInt(STATUS_NOT_READ);
                    // Schrijf de geproduceerde waarde weg
                    out.putInt(newValue);
                    System.out.println("PRODUCER: " + newValue );
                    // De volgende waarde die we uiteindelijk weg willen schrijven
                    newValue++;
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
        System.out.println("PRODUCER: KLAAR" );
    }

}
