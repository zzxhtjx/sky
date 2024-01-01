package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description 配置redis对象
 * @Classname RedisConfiguration
 * @Date 2024/1/1 16:10
 * @Created by dongxuanmang
 */
@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis对象");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置redis的key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
