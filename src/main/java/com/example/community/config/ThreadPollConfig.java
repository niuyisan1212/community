package com.example.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author INSLYAB
 * @Date 2022/7/22 16:29
 */
//ThreadPoolTaskScheduler 需要以下配置才能生效
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPollConfig {
}
