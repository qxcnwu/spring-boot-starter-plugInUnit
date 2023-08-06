package com.qxc.threadexector.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 23:41
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class CheckThreadExectorConfiguration {
    @Value("${check-thread-exector-configuration.multiThread}")
    boolean isThread = false;
    @Value("${check-thread-exector-configuration.threadNum}")
    int threadNum = 1;
}
