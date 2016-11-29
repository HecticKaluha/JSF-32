package countdownlatch;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
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
public class MyLatchRunnable implements Runnable {

    private CountDownLatch cd;
    private static int teller = 0;

    public MyLatchRunnable(CountDownLatch cd) {
        this.cd = cd;
    }

    public void run() {
        int id = teller++;
            System.out.println("STARTED: Runnable: " + id + ", Thread ID: " + Thread.currentThread().getId());

            // loop om te simuleren dat er van alles berekend wordt
            for (long i = 0; i < Math.round(Math.random() * 5000); i++) {
                try {
                    Thread.currentThread().sleep(i);
                } catch (InterruptedException ex) {
                }
            }

            // wacht tot iedereen ook op dit punt is
            cd.countDown();
            
            System.out.println("CONTINUE: Runnable: " + id + ", Thread ID: " + Thread.currentThread().getId());
            // loop om te simuleren dat er van alles berekend wordt
            for (long i = 0; i < Math.round(Math.random() * 500); i++) {
                try {
                    Thread.currentThread().sleep(i);
                } catch (InterruptedException ex) {
                }
            }
        

    }
}
