package com.example.community.dao;

import com.example.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author INSLYAB
 * @Date 2022/6/22 17:31
 */
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    int selectDiscussPostRows(@Param("userId") int userId);
}
