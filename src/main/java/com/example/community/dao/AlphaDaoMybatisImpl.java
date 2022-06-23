package com.example.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @Author INSLYAB
 * @Date 2022/6/21 11:13
 */
@Primary
@Repository
public class AlphaDaoMybatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "AlphaDaoMybatisImpl";
    }
}
