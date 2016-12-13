/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package notification_extended;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erik
 */
public class WatchDirMain {

    public static void main(String[] args)  {
             
        if (args.length == 0 || args.length > 2) {
            // show how to use it
            usage();
        }
        boolean recursive = false;
        int dirArg = 0;
        if (args[0].equals("-r")) {
            if (args.length < 2) {
                // show how to use it
                usage();
            }
            recursive = true;
            dirArg++;
        }
        // create Path to watch from the arguments
        Path dir = Paths.get(args[dirArg]);
        
        try {
            // create WatchDirRunnable object to watch the given directory (and possibly recursive)
            WatchDirRunnable watcher = new WatchDirRunnable(dir, recursive);
            // create Thread and start watching
            new Thread(watcher).start();
            
        } catch (IOException ex) {
            Logger.getLogger(WatchDirMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }
    
}
