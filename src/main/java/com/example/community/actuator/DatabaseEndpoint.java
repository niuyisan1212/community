package com.example.community.actuator;

import com.example.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author INSLYAB
 * @Date 2022/7/23 11:14
 */
@Component
@Endpoint(id = "database")
public class DatabaseEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndpoint.class);

    @Autowired
    private DataSource dataSource;

    @ReadOperation
    public String checkConnection(){
        try(Connection conn = dataSource.getConnection();){
            return CommunityUtil.getJsonString(200, "获取连接成功");
        } catch (SQLException e){
            logger.error("获取连接失败:"+e.getMessage());
            return CommunityUtil.getJsonString(503,"获取连接失败");
        }
    }
}
