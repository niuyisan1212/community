package com.example.community;

import com.example.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${kaptcha.image.width}")
    private String imageWidth;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    void test(){
        String text = "afabc";
        System.out.println(sensitiveFilter.filter(text));
    }
}
