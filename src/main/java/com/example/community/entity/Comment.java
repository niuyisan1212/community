package com.example.community.entity;

import lombok.*;

import java.util.Date;

/**
 * @Author INSLYAB
 * @Date 2022/7/5 8:15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {

    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
