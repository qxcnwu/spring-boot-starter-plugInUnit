package com.qxc.threadexector.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/14 11:15
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class DirectoryTargetMonitorConfiguration {
    /**
     * 监控的根目录
     */
    @Value("${directory-target-monitor-configuration.baseDir}")
    String baseDir;

    @Value("${directory-target-monitor-configuration.listenDir}")
    String listenDir;

    /**
     * 监控刷新时长
     */
    @Value("${directory-target-monitor-configuration.refreshTime:10}")
    int refreshTime;

    @Value("${directory-target-monitor-configuration.busType:PRE_THREAD_EXECUTOR_SERVICE}")
    String busType;

    /**
     * 默认不是用多线程的EVENTBUS
     */
    @Value("${directory-target-monitor-configuration.multiThread:false}")
    boolean multiThread;

    @Value("${directory-target-monitor-configuration.threadNum:-1}")
    int threadNum;

    /**
     * 默认使用异步的bus
     */
    @Value("${directory-target-monitor-configuration.asyncBus:true}")
    boolean asyncBus;


}
