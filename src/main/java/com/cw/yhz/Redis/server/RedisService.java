package com.cw.yhz.Redis.server;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    <T> void put(String key, T obj);

    <T> void put(String key, T obj, int timeout);

    <T> void put(String key, T obj, int timeout, TimeUnit unit);

    <T> T get(String key, Class<T> cls);

    boolean exists(String key);

    void del(String key);

    boolean expire(String key, long timeout, TimeUnit unit);

    boolean expire(String key, long timeout);

    void put(String key, String value);

    void put(String key, String value, int timeout);

    void put(String key, String value, int timeout, TimeUnit unit);

    String get(String key);

    void putHash(String key, Map<Object, Object> m);

    Map<Object, Object> getHash(String key);
}
