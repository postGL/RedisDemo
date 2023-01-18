package com.zbs.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
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

    /**
     * 关于Key“过期时间timeout”
     * 如果用DEL, SET, GETSET会将key对应存储的值替换成新的，命令也会清除掉超时时间；
     * 如果list结构中添加一个数据或者改变hset数据的一个字段是不会清除超时时间的；
     * 如果想要通过set去覆盖值那就必须重新设置expire。
     * 使用set重新设置一个有过期时间的key后，使用ttl命令查看key的过期时间，会发现过期时间又被重置为了-1，即永久不失效。
     */

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

    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        // 存值
        hashOperations.put("002", "name", "xiaoming");
        hashOperations.put("002", "age", "20");
        hashOperations.put("002", "address", "bj");
        // 取值
        String name = (String) hashOperations.get("002", "name");
        System.out.println(name);

        // 获取hash结构中的所有key
        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        // 获取hash结构中的所有值
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

}
