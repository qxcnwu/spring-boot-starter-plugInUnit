package com.qxc.threadexector.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 14:46
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class ServerThreadPoolManageConfiguration {
    @Value("${server-thread-pool-manage-configuration.name:serverThreadPoolManager}")
    String name;
    @Value("${server-thread-pool-manage-configuration.initSize:2}")
    int initSize;
    @Value("${server-thread-pool-manage-configuration.maxSize:6}")
    int maxSize;
    @Value("${server-thread-pool-manage-configuration.coreSize:4}")
    int coreSize;
    @Value("${server-thread-pool-manage-configuration.queueSize:64}")
    int queueSize;
}
