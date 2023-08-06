package com.qxc.threadexector.dirctoryautolisten;

import com.qxc.threadexector.configuration.DirectoryTargetMonitorConfiguration;
import com.qxc.threadexector.eventbus.AsyncEventBus;
import com.qxc.threadexector.eventbus.Dispatcher;
import com.qxc.threadexector.eventbus.EventBus;
import com.qxc.threadexector.threadfactorycore.ThreadPoolFactoryImpl;
import com.qxc.utiles.CurrentSystemInfo;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 11:22
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.dirctoryautolisten
 */
@Component
public class EventBusFactory {
    private final DirectoryTargetMonitorConfiguration configuration;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public EventBusFactory(DirectoryTargetMonitorConfiguration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("MissingJavadoc")
    @Bean
    public EventBus getBus() {
        // 首先更具配置文件创建线程池
        Executor pool;
        switch (configuration.getBusType()) {
            case "SEQ_EXECTOR_SERVICE":
                pool = Dispatcher.SEQ_EXECTOR_SERVICE;
                break;
            case "PRE_THREAD_EXECUTOR_SERVICE":
                pool = Dispatcher.PRE_THREAD_EXECUTOR_SERVICE;
                break;
            default:
                pool = configuration.isMultiThread() ? ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getFixPool("EventBusFactoryThreadPool", configuration.getThreadNum() <= 0 ? CurrentSystemInfo.getInstance().getCPU_Processors() * 2 : configuration.getThreadNum()) : ThreadPoolFactoryImpl.ThreadFactoryCore.getInstance().getSinglePool("EventBusFactoryThreadPool");
                break;
        }
        // 判断是否异步调用
        return configuration.isAsyncBus() ? new AsyncEventBus(pool) : new EventBus(pool);
    }
}
