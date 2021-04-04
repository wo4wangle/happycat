package happycat.watcher;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.io.watch.Watcher;
import happycat.catalina.Host;
import happycat.util.Constant;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

//watch web app folder, if there comes a new war file, then load it
public class WarFileWatcher {
    private WatchMonitor monitor;
    public WarFileWatcher(Host host) {
        this.monitor = WatchUtil.createAll(Constant.webappsFolder, 1, new Watcher() {
            private void dealWith(WatchEvent<?> event, Path currentPath) {
                synchronized (WarFileWatcher.class) {
                    //because many be two war file arrive web app at the same, we need handle it one by one, so we use synchronized
                    String fileName = event.context().toString();
                    if(fileName.toLowerCase().endsWith(".war")  && ENTRY_CREATE.equals(event.kind())) {
                        File warFile = FileUtil.file(Constant.webappsFolder, fileName);
                        host.loadWar(warFile);
                    }
                }
            }
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                dealWith(event, currentPath);
            }
 
            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                dealWith(event, currentPath);
 
            }
            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                dealWith(event, currentPath);
            }
            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                dealWith(event, currentPath);
            }
 
        });
    }
 
    public void start() {
        monitor.start();
    }
 
    public void stop() {
        monitor.interrupt();
    }
 
}