/*
 * Simpel voorbeeld om 2 thread objecten van dezelfde klasse te runnen.
 */

package simple;

/**
 *
 * @author erik
 */
public class ThreadRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws InterruptedException{

        // maak runnable object
        Runnable r1 = new HelloRunnable();
        // maak thread
        Thread t1 = new Thread(r1);  // status NEW
        // start de thread
        t1.start();   // status RUNNABLE
//
//        // start nog een thread van eenzelfde runnable
//        Thread t2 = new Thread(r1);
//        t2.start();
//        //
//        
//        // continu runnable
//        Runnable r2 = new ContinuRunnable();
//        // start 2 threads met de runnable
//        Thread t3 = new Thread(r2);
//        t3.start();
//        Thread t4 = new Thread(r2);
//        t4.start();
//        //
//        //
//        Thread.currentThread().sleep(6500);
//        //
//        t3.interrupt();
//        //
//        Thread.currentThread().sleep(6300);
//        //
//        t4.interrupt();
//        //
//        Thread.currentThread().sleep(4000);
//

    }

}
