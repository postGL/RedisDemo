package com.zbs.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

/**
 * description: JedissonTest
 * date: 2024/3/26 18:45
 * version: 1.0
 * @author zhangbs
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class JedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redissonBucket() {
        RBucket<Object> bucket = redissonClient.getBucket("name");
        // 设置value 10s过期
        bucket.set("张三", Duration.ofSeconds(15L));
        Object value = bucket.get();
        System.out.println(value);
        // 通过key获取value
        Object value2 = redissonClient.getBucket("name").get();
        System.out.println(value2);
    }

}
