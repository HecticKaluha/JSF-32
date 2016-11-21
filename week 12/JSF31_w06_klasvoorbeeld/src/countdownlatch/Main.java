package countdownlatch;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
        CountDownLatch cd = new CountDownLatch(5);
        // maak threadpool
        ExecutorService ex = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 5; i++) {
            ex.execute(new MyLatchRunnable(cd));
        }

        // stop de threads en de pool, is te verplaatsen naar vóór het verwerken
        ex.shutdown();
        
        try {
            // wacht tot alle thread klaar zijn.
            //Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            cd.await();
              //Thread.sleep(1);
            System.out.println("alle threads zijn klaar!");
        } catch (InterruptedException ex1) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
        }




    }
}
