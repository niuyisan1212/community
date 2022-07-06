package com.example.community.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author INSLYAB
 * @Date 2022/7/6 13:03
 */
//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.example.community.service.*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void befor(){

    }

    //After AfterReturning AfterThrowing Around
}
