//package com.zbs.redis;
//
//import redis.clients.jedis.Jedis;
//
///**
// * 使用Jedis连接Redis，进行操作
// * description: RedisTest
// * date: 2023/1/17 14:39
// * author: zhangbs
// * version: 1.0
// */
//public class RedisTest {
//    public static void main(String[] args) {
//        // 连接本地的Redis服务
//        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        jedis.auth("root");
//        String response = jedis.ping("ping ping,pong pong");
//        System.out.println(response);
//    }
//}
