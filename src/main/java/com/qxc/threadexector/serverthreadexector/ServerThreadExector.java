package com.qxc.threadexector.serverthreadexector;

import com.qxc.threadexector.configuration.ServerThreadPoolManageConfiguration;
import com.qxc.threadexector.construct.BasicThreadPool;
import com.qxc.threadexector.ThreadPoolManage;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 14:49
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.serverthreadexector
 */
@Component
@Data
public class ServerThreadExector {
    private final ThreadPoolManage pool;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    public ServerThreadExector(@NotNull ServerThreadPoolManageConfiguration configuration) {
        pool = ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getThreadPoolMange(configuration.getName(), configuration.getInitSize(), configuration.getMaxSize(), configuration.getCoreSize(), configuration.getQueueSize());
    }
}
