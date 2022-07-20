package com.example.community.service;

import com.example.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author INSLYAB
 * @Date 2022/7/20 10:46
 */
@Service
public class DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    public void recordUV(String ip){
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    public long calculateUV(Date start, Date end){
        if(start == null || end == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while(!calendar.getTime().after(end)){
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }
        String redisKey = RedisKeyUtil.getUVKey(df.format(start), df.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey, keyList.toArray());
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    public void recordDAU(int userId){
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    public long calculateDAU(Date start, Date end){
        if(start == null || end == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while(!calendar.getTime().after(end)){
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }
        String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
        return (long) redisTemplate.execute((RedisCallback) connection -> {
            String redisKey1 = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
            connection.bitOp(RedisStringCommands.BitOperation.OR,
                    redisKey1.getBytes(), keyList.toArray(new byte[0][0]));
            return connection.bitCount(redisKey1.getBytes());
        });
    }
}
