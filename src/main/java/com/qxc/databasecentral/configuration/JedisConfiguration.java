package com.qxc.databasecentral.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author qxc
 * @Date 2023 2023/7/15 16:51
 * @Version 1.0
 * @PACKAGE com.qxc.databasecentral.configuration
 */
@Configuration
@PropertySource("classpath:DataBase.properties")
@Data
public class JedisConfiguration {
    @Value("${redis.host:127.0.0.1}")
    private String host;
    @Value("${redis.port:6379}")
    private Integer port;
    @Value("${redis.maxIdle:300}")
    private Integer maxIdle;
    @Value("${redis.maxActive:600}")
    private Integer maxActive;
    @Value("${redis.maxWait:1000}")
    private Integer maxWait;
    @Value("${redis.testOnBorrow:true}")
    private boolean testOnBorrow;

    @SuppressWarnings("MissingJavadoc")
    @Bean
    public RedisTemplate<String, String> getJedisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxTotal(maxActive);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();

        RedisTemplate<String, String> answer = new RedisTemplate<>();
        answer.setConnectionFactory(jedisConnectionFactory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        answer.setValueSerializer(redisSerializer);
        answer.setKeySerializer(redisSerializer);
        return answer;
    }
}
