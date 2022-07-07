package com.example.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Author INSLYAB
 * @Date 2022/7/7 11:03
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //设置key 的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置value 的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置hash key 的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash value 的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }
}
