package com.qxc.threadexector.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/18 21:25
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class ThreadFactoryCoreCleanConfiguration {
    @Value("${thread-factory-core-clean-configuration.cleantime:20}")
    Integer time;
}
