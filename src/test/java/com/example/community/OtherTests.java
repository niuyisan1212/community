package com.example.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Author INSLYAB
 * @Date 2022/6/25 18:32
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class OtherTests {

    @Value("${community.path.domain}")
    private String DOMAIN;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @Test
    void test(){
        System.out.println(DOMAIN);
        System.out.println(CONTEXT_PATH);
    }
}
