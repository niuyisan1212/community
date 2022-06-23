package com.example.community.entity;

import lombok.*;

import java.util.Date;

/**
 * @Author INSLYAB
 * @Date 2022/6/22 17:29
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
