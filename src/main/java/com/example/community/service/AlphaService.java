package com.example.community.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author INSLYAB
 * @Date 2022/6/21 11:17
 */
@Service
public class AlphaService {

    public AlphaService(){
        System.out.println("实例化AlphaService方法");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService方法");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("销毁AlphaService方法");
    }
}
