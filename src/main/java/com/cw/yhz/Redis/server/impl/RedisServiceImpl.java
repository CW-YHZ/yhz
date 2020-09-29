package com.cw.yhz.Redis.server.impl;

import com.alibaba.fastjson.JSON;
import com.ouhl.utildemo.Redis.server.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public <T> void put(String key, T obj) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
    }

    @Override
    public <T> void put(String key, T obj, int timeout) {
        put(key,obj,timeout, TimeUnit.MINUTES);
    }

    @Override
    public <T> void put(String key, T obj, int timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj),timeout,unit);
    }

    @Override
    public <T> T get(String key, Class<T> cls) {
        return JSON.parseObject(JSON.toJSONString(redisTemplate.opsForValue().get(key)), cls);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
    }

    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, String value, int timeout) {
        put(key,value,timeout,TimeUnit.MINUTES);
    }

    @Override
    public void put(String key, String value, int timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void putHash(String key, Map<Object, Object> m) {
        redisTemplate.opsForHash().putAll(key, m);
    }

    @Override
    public Map<Object, Object> getHash(String key) {
        try{
            return redisTemplate.opsForHash().entries(key);
        }catch(Exception e){
            return null;
        }
    }
}
