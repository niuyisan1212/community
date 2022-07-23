package com.example.community;

import com.example.community.entity.DiscussPost;
import com.example.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

/**
 * @Author INSLYAB
 * @Date 2022/7/22 17:14
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CaffeineTests {

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    void test01(){
        for(int i = 0; i < 10000; i++){
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("压力测试title");
            post.setContent("这是一个压力测试的content");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            discussPostService.addDiscussPost(post);

            System.out.println(i);
        }
    }

    @Test
    void test02(){
        discussPostService.findDiscussPosts(0, 0, 10, 1);
        discussPostService.findDiscussPosts(0, 0, 10, 1);
        discussPostService.findDiscussPosts(0, 0, 10, 1);
    }
}
