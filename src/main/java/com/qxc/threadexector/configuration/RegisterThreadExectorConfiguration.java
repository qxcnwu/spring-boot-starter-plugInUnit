package com.qxc.threadexector.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 14:01
 * @Version 1.0
 * @PACKAGE com.qxc.threadexector.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class RegisterThreadExectorConfiguration {
    @Value("${register-thread-exector-configuration.multiThread:true}")
    boolean multiThread;
    @Value("${register-thread-exector-configuration.multiThreadNumber:2}")
    int multiThreadNumber;
}
