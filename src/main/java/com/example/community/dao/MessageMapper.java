package com.example.community.dao;

import com.example.community.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author INSLYAB
 * @Date 2022/7/6 9:10
 */
@Mapper
public interface MessageMapper {
    //查询当前用户会话列表
    List<Message> selectConversations(int userId, int offset, int limit);
    //查询当前用户会话数量
    int selectConversationCount(int userId);

    //查询某个会话包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);
    //查询某个会话包含的私信列表数量
    int selectLetterCount(String conversationId);

    //查询未读消息数量
    int selectLetterUnreadCount(int userId, String conversationId);

    //新增一条消息
    int insertMessage(Message message);

    //修改消息状态
    int updateStatus(List<Integer> ids, int status);
}
