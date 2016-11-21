package threadpools;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erik
 */
public class TijdRunnable implements Runnable{

    public void run() {
        try {
            // print iets naar standaard output
            System.out.println("Thread: " + Thread.currentThread().getId() + " Tijd:" + System.currentTimeMillis());
            // wacht
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(TijdRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
