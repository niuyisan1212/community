package com.example.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Author INSLYAB
 * @Date 2022/6/21 15:33
 */
@Configuration
public class AlphaConfig {

    @Bean
    public SimpleDateFormat sinmpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
