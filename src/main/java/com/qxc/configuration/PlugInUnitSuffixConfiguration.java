package com.qxc.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 20:36
 * @Version 1.0
 * @PACKAGE com.qxc.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class PlugInUnitSuffixConfiguration {
    @Value("${plugin-unit-suffix-configuration.server:server}")
    String server;

    @Value("${plugin-unit-suffix-configuration.doc:doc}")
    String doc = "doc";

    @Value("${plugin-unit-suffix-configuration.src:src}")
    String src = "src";

    @Value("${plugin-unit-suffix-configuration.static:static}")
    String statics = "static";

    @Value("${plugin-unit-suffix-configuration.config:config.json}")
    String config = "config.json";
}
