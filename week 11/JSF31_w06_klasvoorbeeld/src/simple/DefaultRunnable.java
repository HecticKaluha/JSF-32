package simple;

/**
 * Default structuur voor Runnable die gebruikt kan worden als basis
 * voor je eigen threads.
 * @author erik
 */
public class DefaultRunnable implements Runnable{
    private boolean moreWork = true;

    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted() && moreWork){
                // do work

                // cooperative multitasking. not necessary in preemptive JVM's
                Thread.yield();


                // also cooperative. Generates InterruptedException when interrupted bit has been set.
                Thread.sleep(0);
            }
        }
        catch(InterruptedException e){
            // thread interrupted during 'sleep' or 'wait'
            System.out.println("interrupted during sleep or wait");        }
        finally{
            // cleanup, if required
        }

    }
    
    public void noMoreWork(){
        this.moreWork = false;
    }

}
