package com.example.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @Author INSLYAB
 * @Date 2022/6/21 11:10
 */
@Repository
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "AlphaDaoHibernateImpl";
    }
}
