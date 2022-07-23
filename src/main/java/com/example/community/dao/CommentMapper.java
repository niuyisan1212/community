package com.example.community.dao;

import com.example.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author INSLYAB
 * @Date 2022/7/5 8:17
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

    //查找用户评论的所有帖子
    List<Comment> selectCommentByUserId(int userId, int offset, int limit);

    int selectCountByUserId(int userId);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
