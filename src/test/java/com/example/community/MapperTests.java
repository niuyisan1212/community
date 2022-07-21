package com.example.community;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.LoginTicketMapper;
import com.example.community.dao.MessageMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.LoginTicket;
import com.example.community.entity.Message;
import com.example.community.entity.User;
import com.sun.corba.se.pept.protocol.MessageMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
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

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void test1(){
       User user = userMapper.selectById(101);
       System.out.println(user);
    }

    @Test
    void test2(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10,0);
        for(DiscussPost discussPost : discussPosts){
            System.out.println(discussPost);
        }
        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }

    @Test
    void test3(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abc",1);
    }

    @Test
    void test4(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for(Message message: messages){
            System.out.println(message);
        }
        System.out.println(messageMapper.selectConversationCount(111));
        messages = messageMapper.selectLetters("111_112", 0, 10);
        for(Message message: messages){
            System.out.println(message);
        }
        messageMapper.selectLetterCount("111_112");
        messageMapper.selectLetterUnreadCount(111, null);
    }
}
