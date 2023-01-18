package com.zbs.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 存值
        valueOperations.set("zhangsan", "25");
        // 取值
        String value = (String) valueOperations.get("zhangsan");
        System.out.println(value);

        // 设置key的有效期
        valueOperations.set("lisi", "55", 100l, TimeUnit.SECONDS);

        // set添加一条数据，不会影响其他key的超时时间！
        //  valueOperations.set("lisi2", "55", 100l, TimeUnit.SECONDS);
        // 覆盖数值55，如果不设置过期时间，则默认-1，“不失效”，如果设置会覆盖
        valueOperations.set("lisi", "56", 100l, TimeUnit.SECONDS);

        // setIfAbsent：如果没有就新增，如果有就替换
        Boolean result = valueOperations.setIfAbsent("city", "beijing");
        System.out.println(result);
    }

}
