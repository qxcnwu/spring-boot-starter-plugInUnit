package com.qxc.databasecentral.configuration;

import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 16:36
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.configuration
 */
@Configuration
@Component
@Data
public class JdbcTemplateConfiguration {

    private final DataSource dataSource;

    @SuppressWarnings("MissingJavadoc")
    @Contract(pure = true)
    @Autowired
    public JdbcTemplateConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("MissingJavadoc")
    @Bean("jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        //设置数据源
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }
}
