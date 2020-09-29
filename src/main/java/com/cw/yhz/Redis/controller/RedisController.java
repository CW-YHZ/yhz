package com.cw.yhz.Redis.controller;

import com.ouhl.utildemo.Redis.server.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ouhl/redis")
public class RedisController {

    @Autowired
    private RedisServiceImpl redisServer;

    @RequestMapping(value="set")
    @ResponseBody
    public String set(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisServer.put(key, value);
        return "存值成功";
    }

    @RequestMapping(value="get")
    @ResponseBody
    public String get(@RequestParam("key") String key) {
        return "取值成功，值为："+redisServer.get(key);
    }

    @RequestMapping(value="del")
    @ResponseBody
    public String del(@RequestParam("key") String key) {
        redisServer.del(key);
        return "删除成功！";
    }
}
