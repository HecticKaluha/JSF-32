package threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import simple.HelloRunnable;

/**
 *
 * @author erik
 */
public class Threadpools {

    public static void main(String[] args) throws Exception
   {
      final int MAXINPOOL = 5;
      final int AMOUNTOFWORK = 103;
      // create cached threadpool
      ExecutorService pool = Executors.newCachedThreadPool();//.newFixedThreadPool(MAXINPOOL);

      // add tasks to print Hello to standard output to threadpool.
      for(int i=0;i<AMOUNTOFWORK;i++){
          HelloRunnable runner = new HelloRunnable();
          // submit to pool and execute
          pool.submit(runner);
      }

      // info over wat er in de pool gebeurd.
      for(int i=0;i<50;i++){
        // print hoeveelheid werk en aantal threads in pool
             int aantalwerkend = ((ThreadPoolExecutor) pool).getActiveCount();
             int aantalinqueue = ((ThreadPoolExecutor) pool).getQueue().size();
             int maxaantalinpool = ((ThreadPoolExecutor) pool).getMaximumPoolSize();
             long aantalverwerkt = ((ThreadPoolExecutor) pool).getCompletedTaskCount();
             System.out.println("max, in queue, actief, verwerkt: "+maxaantalinpool+", "+aantalinqueue+", "+aantalwerkend+", "+aantalverwerkt);
          // wacht
        Thread.sleep(500);

      }


      //
      // stop the pool
      pool.shutdown();

   }

}
