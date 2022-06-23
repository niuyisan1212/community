package com.example.community.entity;

import lombok.*;

import java.util.Date;

/**
 * @Author INSLYAB
 * @Date 2022/6/22 16:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
