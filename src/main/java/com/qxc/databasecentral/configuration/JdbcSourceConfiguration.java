package com.qxc.databasecentral.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 16:37
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.configuration
 */
@Configuration
@PropertySource("classpath:DataBase.properties")
@Data
public class JdbcSourceConfiguration {
    @Value("${jdbc.driver:com.mysql.jdbc.Driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.initialSize:5}")
    int initialSize;
    @Value("${jdbc.maxActive:10}")
    int maxActive;
    @Value("${jdbc.maxWait:3000}")
    int maxWait;

    @SuppressWarnings("MissingJavadoc")
    @Bean("dataSource")
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setInitialSize(initialSize);
        return dataSource;
    }
}
