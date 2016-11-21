/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package notifywaitexample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Boots
 */
public class BlockingQueueExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        queue.put(i);
                        System.out.println("PUT: " + i);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BlockingQueueExample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }, "Producer").start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println("TAKE: " + queue.take());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BlockingQueueExample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }, "Consumer").start();
    }
}
