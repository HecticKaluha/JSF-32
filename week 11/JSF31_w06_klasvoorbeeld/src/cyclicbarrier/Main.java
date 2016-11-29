package cyclicbarrier;

import java.util.concurrent.CyclicBarrier;
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
        final int NROFTHREADS = 34;
        // creeer cyclic barrier
        CyclicBarrier cb = new CyclicBarrier(5);

        // maak threadpool
        ExecutorService ex = Executors.newFixedThreadPool(NROFTHREADS);

        for (int i = 0; i < NROFTHREADS; i++) {
            ex.execute(new MyCBRunnable(cb));
        }
        // stop de threads en de pool
        ex.shutdown();


 
        


    }
}
