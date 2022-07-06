package com.example.community.entity;

import lombok.*;

import java.util.Date;

/**
 * @Author INSLYAB
 * @Date 2022/7/6 9:08
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {

    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
}
