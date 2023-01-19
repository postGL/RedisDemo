package com.zbs.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
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

    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        // 存值(从左边插入值)
        listOperations.leftPush("myList", "a");
        listOperations.leftPushAll("myList", "b", "c", "d");

        // 取值
        List<String> myList = listOperations.range("myList", 0, -1);
        for (String value : myList) {
            System.out.println(value);
        }

        // 获取列表长度
        Long size = listOperations.size("myList");
        int lSize = size.intValue();
        for (int i = 0; i < lSize - 1; i++) {
            System.out.println("============");
            String element = (String) listOperations.rightPop("myList");
            System.out.println(element);
        }

        System.out.println("============");
        System.out.println(myList);
    }

    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        // 存值
        setOperations.add("mySet", 22, 25, 26);

        // 取值
        Set<Integer> mySet = setOperations.members("mySet");
        for (Integer i : mySet) {
            System.out.println(i);
        }

        // 删除成员，返回值1或0
        Long mySet1 = setOperations.remove("mySet", 26);
        System.out.println(mySet1);

        // 取值
        Set<Integer> mySet2 = setOperations.members("mySet");
        for (Integer i : mySet2) {
            System.out.println(i);
        }
    }

    /**
     * Redis 有序集合和集合一样也是 string 类型元素的集合,且不允许重复的成员。
     * 不同的是每个元素都会关联一个 double 类型的分数。redis 正是通过分数来为集合中的成员进行从小到大的排序。
     * 有序集合的成员是唯一的,但分数(score)却可以重复。
     * 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。 集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。
     */
    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        // 存值
        zSetOperations.add("myZset", "oracle", 12);
        zSetOperations.add("myZset", "mysql", 15);
        zSetOperations.add("myZset", "sqlserver", 18);

        // 按照index从小到大排序  1.key  2.从0开始 3.到-1  相对于输出全部
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }

        // 修改分数
        zSetOperations.incrementScore("myZset", "mysql", 20);

        Set<String> myZset2 = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset2) {
            System.out.println(s);
        }

        // 删除成员
        zSetOperations.remove("myZset", "oracle", "mysql");

        Set<String> myZset3 = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset3) {
            System.out.println(s);
        }

    }

}
