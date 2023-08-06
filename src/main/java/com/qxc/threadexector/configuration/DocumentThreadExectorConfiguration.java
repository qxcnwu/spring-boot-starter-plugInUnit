package com.qxc.threadexector.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 20:42
 * @Version 1.0
 * @PACKAGE com.qxc.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class DocumentThreadExectorConfiguration {
    @Value("${document-thread-exector.multiThread}")
    boolean multiThread = true;
    @Value("${document-thread-exector.multiThreadNumber}")
    int multiThreadNumber = 2;
    @Value("${document-thread-exector.exectorLimitTime}")
    int exectorLimitTime = 10;
    @Value("${document-thread-exector.exectorLimitNumber}")
    int exectorLimitNumber=10;
    @Value("${document-thread-exector.RSTPath}")
    String RSTPath;
    @Value("${document-thread-exector.RSTCommand}")
    String RSTCommand;
    @Value("${document-thread-exector.execDir}")
    String execDir;
}
