package com.qxc.configuration;

import com.qxc.threadexector.configuration.CheckThreadExectorConfiguration;
import com.qxc.threadexector.configuration.DocumentThreadExectorConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 19:02
 * @Version 1.0
 * @PACKAGE com.qxc.configuration
 */
@Configuration
@ComponentScan("com.qxc")
@Import({CheckThreadExectorConfiguration.class, DocumentThreadExectorConfiguration.class,BaseConfiguration.class})
public class SpringConfiguration {

}
