package threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author erik
 */
public class TimerGebruik {

    public static void main(String[] args) throws Exception {
 
        // pool voor
        ScheduledExecutorService timerpool = Executors.newSingleThreadScheduledExecutor();

            Runnable runner = new TijdRunnable();
            // submit to pool and execute
            int wachttijdvoorbegin = 1000;
            int tijdertussen = 2500;
            // met vaste tijd tussen eind van uitvoering en begin van uitvoering van threads
            //timerpool.scheduleWithFixedDelay(runner, wachttijdvoorbegin, tijdertussen, TimeUnit.MILLISECONDS);

            // met vaste tijd tussen begin van uitvoeringen van threads
            timerpool.scheduleAtFixedRate(runner, tijdertussen, tijdertussen, TimeUnit.MILLISECONDS);
        // wacht
        Thread.sleep(20000);
        // ruim pool op
        timerpool.shutdown();
    }
}
