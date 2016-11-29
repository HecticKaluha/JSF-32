/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package os_callablethreadpool;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author P.Boots
 */
public class Main {
   /**
     * @param args the command line arguments
     */

    public static void main(String[] args){
        // gebruik callables (foute manier)
        //callable();
        // gebruik futures (da right way)
        future();

    }



    /**
     * @param args the command line arguments
     */
    /**
     * gebruik callables om resultaat op te vragen van een thread.
     * Dit is niet de goede manier!!! (want Callable zonder Future!)
     */
    private static void callable() {
        // maak threadpool
        ExecutorService ex = Executors.newFixedThreadPool(8);

        // maak lijst van callables
        ArrayList<Callable> callables = new ArrayList<Callable>();

        for (int i = 0; i < 15; i++) {
            Callable c = new MyCallable();
            // voeg toe aan verzameling
            callables.add(c);
            // submit in threadpool
            ex.submit(c);
        }

        // verwerk resultaten
            //
            for(Callable c : callables){
                try {
                    System.out.println(c.call());
                } catch (Exception ex1) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }


  

        // stop de threads en de pool
        ex.shutdown();

    }


    // de juiste manier!
    public static void future(){
        // maak threadpool
        ExecutorService ex = Executors.newFixedThreadPool(8);
        // maak lijst voor resultaten van callables
        ArrayList<Future<String>> resultaten = new ArrayList<Future<String>>();

        for (int i = 0; i < 20000; i++) {
            Future<String> f = ex.submit(new MyCallable());
            resultaten.add(f);
        }       
        //
        // verwerk resultaten, blocking aanroep van get()!
        // dus kan vaak blocked raken!
        try {
            //
            for(Future<String> f : resultaten){
                System.out.println(f.get());
            }

        // stop de threads en de pool, is te verplaatsen naar vóór het verwerken
        // DEMO
        ex.shutdown();
        // slaaaaap 2sec
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex1) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
        }
        // verzoek garbage collector
        System.gc();

        
            
        } catch (InterruptedException ex1) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (ExecutionException ex1) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
        }


     
   
    }


}
