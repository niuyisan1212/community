package com.example.community.service;

import com.example.community.dao.UserMapper;
import com.example.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author INSLYAB
 * @Date 2022/6/22 19:49
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
