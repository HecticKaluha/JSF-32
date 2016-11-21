package cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author P.Boots
 *
 */
public class MyCBRunnable implements Runnable {

    private CyclicBarrier cb;
    private static int teller = 0;

    public MyCBRunnable(CyclicBarrier cb) {
        this.cb = cb;
    }

    public void run() {
        int id = teller++;
        try {
            while (true) {
                System.out.println("STARTED: Runnable: " + id + ", Thread ID: " + Thread.currentThread().getId());

                // loop om te simuleren dat er van alles berekend wordt
                for (long i = 0; i < Math.round(Math.random() * 5000); i++) {
                    try {
                        Thread.currentThread().sleep(i);
                    } catch (InterruptedException ex) {
                    }
                }

                // wacht op de andere threads
                cb.await();


                System.out.println("CONTINUE: Runnable: " + id + ", Thread ID: " + Thread.currentThread().getId());
                // loop om te simuleren dat er van alles berekend wordt
                for (long i = 0; i < Math.round(Math.random() * 500); i++) {
                    try {
                        Thread.currentThread().sleep(i);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(MyCBRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
            Logger.getLogger(MyCBRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
