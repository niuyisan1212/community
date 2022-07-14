package com.example.community.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author INSLYAB
 * @Date 2022/7/14 16:17
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {

    private String topic;
    private int userId;
    private int entityType;
    private int entityID;
    private int entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public Event setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
