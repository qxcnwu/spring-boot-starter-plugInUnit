package com.qxc.threadexector.threadfactorycore;

import com.qxc.threadexector.configuration.ThreadFactoryCoreCleanConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author qxc
 * @Date 2023 2023/7/18 21:24
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.threadfactorycore
 */
@Component
@Slf4j
public class ThreadFactoryCoreClean {
    private final ThreadFactoryCoreCleanConfiguration configuration;
    private static final ThreadPoolFactory TH = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance();
    private static final ExecutorService SINGLE_POOL = TH.getSinglePool("ThreadFactoryCoreClean-Thread");

    @Contract(pure = true)
    @Autowired
    public ThreadFactoryCoreClean(ThreadFactoryCoreCleanConfiguration configuration) {
        this.configuration = configuration;
        SINGLE_POOL.submit(this::init);
    }

    private void init() {
        while (!SINGLE_POOL.isShutdown()) {
            final HashMap<String, Integer> clearAns = TH.clear();
            log.warn("ThreadFactoryCoreClean Clear:" + clearAns);
            try {
                TimeUnit.SECONDS.sleep(configuration.getTime());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }
}
