package os_callablethreadpool;


import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author P.Boots
 */
public class MyCallable implements Callable<String>{
    private static int teller = 0;
    public String call(){
        String returnvalue = "empty value";
        // loop om te simuleren dat er van alles berekend wordt
        for(long i=0;i<Math.round(Math.random()*500);i++){
            try {
                Thread.currentThread().sleep(i);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyCallable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        returnvalue = "Callable:  "+ (teller++) +", Thread ID: "+ Thread.currentThread().getId();
        return returnvalue;
    }
}
