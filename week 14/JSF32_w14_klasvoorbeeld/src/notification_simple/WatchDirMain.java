package notification_simple;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.util.logging.*;

public class WatchDirMain {

    public static void main(String[] args) {
        final WatchService watcher;
        // Voorbeelden van interessante locaties
        // Path dir = Paths.get("D:\\");
        Path dir = Paths.get("/tmp");
        WatchKey key;

        try {
            watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            
            while (true) {
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    
                    Path filename = ev.context();
                    Path child = dir.resolve(filename);
                    
                    WatchEvent.Kind kind = ev.kind();
                    if (kind == ENTRY_CREATE) {
                        System.out.println(child + " created");
                    }
                    if (kind == ENTRY_DELETE) {
                        System.out.println(child + " deleted");
                    }
                    if (kind == ENTRY_MODIFY) {
                        System.out.println(child + " modified");
                    }
                }
                key.reset();
            }

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WatchDirMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
