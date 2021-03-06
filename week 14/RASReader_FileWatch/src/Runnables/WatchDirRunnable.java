package Runnables;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 * Example to watch a directory (or tree) for changes to files.
 */
public class WatchDirRunnable implements Runnable {

    private final WatchService watcher;  // the service object who processes events for us
    private final Map<WatchKey, Path> keys; // the map of WatchKey's and belonging Path
    private final boolean recursive;
    private boolean trace = false;
    private JSF31KochFractalFX application = null;

    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchDirRunnable(Path dir, boolean recursive, JSF31KochFractalFX application) throws IOException {
        // create a default WatchService
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();  // map of watchkeys and path belonging to it
        this.recursive = recursive;
        this.application = application;

        // register all Paths to watch
        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    @SuppressWarnings("unchecked")
    /**
     * utility method to get the context out of the WatchEvent
     */
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService First, create a
     * WatchKey object which connects all event types to a certain path, and add
     * it to the WatchService. keep an own Map for tracing
     *
     */
    private void register(Path dir) throws IOException {

        
        
        WatchKey key = dir.register(this.watcher, ENTRY_CREATE, ENTRY_MODIFY);
        System.out.println(key + " registered on " + dir);
        if (trace)
        {
            Path prev = keys.get(key);
            System.out.println(prev + " get from " + key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
                
                
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
        System.out.println(key + "put to map");
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    /**
     * Process all events for keys queued to the watcher.
     */
    public void run() {
        for (;;)
        {

            // wait for key to be signalled
            WatchKey key =null;
            try {
                //            try {
                key = watcher.take();
                System.out.println(key + "taken from watcher");
//            } catch (InterruptedException x) {
//                return;
//            }
            } catch (InterruptedException ex) {
                Logger.getLogger(WatchDirRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }

            Path dir = keys.get(key);
            System.out.println(dir.toString() + "got path from map");
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents())
            {
                // get event kind
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;  // something unexpected happened, let's ignore this
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path filename = ev.context();
                final Path child = dir.resolve(filename);

                if(!child.toString().equals("C:\\Users\\Milton van de Sanden\\Documents\\GitHub\\JSF-32\\week 14\\RASReader_FileWatch\\edges.bin"))
                {
                    // or you could use: 
                    // Path filename = (Path) event.context();
                    // this leads to the same result as ev.context() below

                    if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY)
                        {
                            System.out.println("event kind is create or modify");
                            //System.out.println(child + " created");
    //                        Platform.runLater
    //                        (
    //                                new Runnable()
    //                                {
    //                                    @Override
    //                                    public void run()
    //                                    {
                                            application.readEdges(child.getFileName().toFile());
    //                                    }
    //                                }
    //                        );
                        }

                    // print out event
                    System.out.format("%s: %s\n", event.kind().name(), child);

                    // if directory is created, and watching recursively, then
                    // register it and its sub-directories
                    if (recursive && (kind == ENTRY_CREATE)) {
                        try {
                            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                                registerAll(child);
                            }
                        } catch (IOException x) {
                            // ignore to keep sample readable
                        }
                    }                    
                }
            }

            // reset key (because you just handled it, and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
}
