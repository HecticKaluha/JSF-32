package os_runnablethreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erik
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creeer threadpool
        ExecutorService ex = Executors.newFixedThreadPool(8);
        // stop runnables erin
        for (int i = 0; i < 20; i++) {
            ex.execute(new MyRunnable());
        }

        // wacht een tijdje
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException ex1) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
        }
        // stop de threads en de pool
        ex.shutdown();

    }
}
