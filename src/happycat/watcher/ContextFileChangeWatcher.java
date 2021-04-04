package happycat.watcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.log.LogFactory;
import happycat.catalina.Context;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
//watch class file update and reload context
public class ContextFileChangeWatcher {
 
    private WatchMonitor monitor;
 
    private boolean stop = false;
 
    public ContextFileChangeWatcher(Context context) {
        this.monitor = WatchUtil.createAll(context.getDocBase(), Integer.MAX_VALUE, new Watcher() {
            private void dealWith(WatchEvent<?> event) {
                synchronized (ContextFileChangeWatcher.class) {
                    //one change may contain several events, but we just need to handle one time
                    String fileName = event.context().toString();
                    if (stop)
                        return;
                    if (fileName.endsWith(".jar") || fileName.endsWith(".class") || fileName.endsWith(".xml")) {
                        stop = true;
                        LogFactory.get().info(ContextFileChangeWatcher.this + " detected file change in web application:{} " , fileName);
                        context.reload();
                    }
 
                }
            }
 
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                dealWith(event);
            }
 
            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                dealWith(event);
 
            }
 
            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                dealWith(event);
 
            }
 
            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                dealWith(event);
            }
 
        });
 
        this.monitor.setDaemon(true);
    }
 
    public void start() {
        monitor.start();
    }
 
    public void stop() {
        monitor.close();
    }
}