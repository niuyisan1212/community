package com.example.community;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @Author INSLYAB
 * @Date 2022/6/22 17:11
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void test1(){
       User user = userMapper.selectById(101);
       System.out.println(user);
    }

    @Test
    void test2(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost discussPost : discussPosts){
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
