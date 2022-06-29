package com.example.community.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author INSLYAB
 * @Date 2022/6/27 21:06
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

}
