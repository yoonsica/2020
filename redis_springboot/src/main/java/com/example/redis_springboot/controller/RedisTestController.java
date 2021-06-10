package com.example.redis_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname RedisTestController
 * @Description TODO
 * @Date 2021/6/10 上午10:45
 * @Author shengli
 */
@RestController
@RequestMapping("/redisTest")
public class RedisTestController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public String testRedis() {
        redisTemplate.opsForValue().set("name", "vic");
        return (String) redisTemplate.opsForValue().get("name");
    }
}
