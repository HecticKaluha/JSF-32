package os_runnablethreadpool;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author P.Boots
 *
 */
public class MyRunnable implements Runnable{
    private static int teller = 0;
    public void run(){
        System.out.println("Runnable: "+teller+ ", Thread ID: "+Thread.currentThread().getId());
        teller++;
    }

}
