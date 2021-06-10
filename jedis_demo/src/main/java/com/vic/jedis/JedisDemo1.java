package com.vic.jedis;

import redis.clients.jedis.Jedis;

/**
 * @Classname JedisDemo1
 * @Description TODO
 * @Date 2021/6/8 下午2:26
 * @Author shengli
 */
public class JedisDemo1 {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        System.out.println(jedis.ping());
    }
}
