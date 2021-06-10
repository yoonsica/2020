package com.vic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * @Classname JedisDemoTest
 * @Description TODO
 * @Date 2021/6/8 下午2:52
 * @Author shengli
 */
public class JedisDemoTest {
    Jedis jedis;

    @Before
    public void context() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    @After
    public void clear() {
        jedis.close();
    }

    @Test
    public void testKeyOp() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Set<String> keys = jedis.keys("*");
        keys.forEach(System.out::println);
    }

    @Test
    public void testList() {
        jedis.lpush("key1","lucy","mary","jack");
        List<String> list = jedis.lrange("key1", 0, -1);
        list.forEach(System.out::println);
    }

    @Test
    public void testSet() {
        jedis.sadd("name", "lucy", "jack", "marry");
        Set<String> names = jedis.smembers("name");
        names.forEach(System.out::println);
    }

    @Test
    public void testHSet() {
        jedis.hset("users", "age", "20");
        System.out.println(jedis.hget("users", "age"));
    }

    @Test
    public void testZSet() {
        jedis.zadd("China", 100, "Shanghai");
        Set<String> zset = jedis.zrange("China", 0, -1);
        zset.forEach(System.out::print);
    }
}
