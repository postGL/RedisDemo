package com.zbs.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * description: SpringDataRedisTest
 * date: 2023/1/17 17:13
 * author: zhangbs
 * version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() {

        redisTemplate.opsForValue().set("zhangsan", "25");
        String value = (String) redisTemplate.opsForValue().get("zhangsan");
        System.out.println(value);

        // 设置key的有效期
        redisTemplate.opsForValue().set("lisi", "55", 10l, TimeUnit.SECONDS);

        // setIfAbsent：如果没有就新增，如果有就替换
        Boolean result = redisTemplate.opsForValue().setIfAbsent("city", "beijing");
        System.out.println(result);

    }

}
