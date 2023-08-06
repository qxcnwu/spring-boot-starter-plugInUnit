package com.qxc.threadexector.dirctoryautolisten;

import com.qxc.threadexector.configuration.DirectoryTargetMonitorConfiguration;
import com.qxc.threadexector.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 11:13
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.dirctoryautolisten
 */
@Component
@Slf4j
public class DirectoryTargetMonitor extends Thread {
    private final int time;
    private WatchService watchService;
    private final EventBus eventBus;
    private final Path path;
    private volatile boolean start;
    private final HashSet<String> set = new HashSet<>();

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public DirectoryTargetMonitor(@NotNull DirectoryTargetMonitorConfiguration configuration, EventBus eventBus) {
        this.eventBus = eventBus;
        path = Path.of(configuration.getListenDir());
        time = configuration.getRefreshTime();
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE);
            log.info("watchService init has been done!!");
        } catch (IOException e) {
            log.error("watchService start error " + e.getMessage());
        }
        start = true;
        start();
    }

    /**
     * 执行监控进程，在其他线程上开始，并非在主线程
     */
    @Override
    public void run() {
        while (start) {
            WatchKey watchKey = null;
            try {
                watchKey = watchService.poll(time, TimeUnit.SECONDS);
                if (watchKey == null) {
                    continue;
                }
                watchKey.pollEvents().forEach(event -> {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path path = (Path) event.context();
                    Path child = DirectoryTargetMonitor.this.path.resolve(path);
                    if (!set.contains(child.toString())) {
                        set.add(child.toString());
                        eventBus.post(child.toString(), kind.name());
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.start = false;
            } finally {
                if (watchKey != null) {
                    watchKey.reset();
                }
            }
        }
    }

    /**
     * 关闭当前的观察者线程
     *
     */
    public void stopMonitor() throws IOException {
        this.start = false;
        this.watchService.close();
        log.info("stopMonitor success");
    }
}
