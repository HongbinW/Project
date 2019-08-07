package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;  //ShardedJedis连接池
    //具体的值，看源码默认值
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));    //控制jedis连接池和redis-server的最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));     //在JedisPool中最大idle(空闲)状态的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));     //在JedisPool中最idle(空闲)状态的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));    //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值为true，则得到的jedis实例肯定是可以用的
    //下面代码已经处理了是否是BrokenResource，因此此处不需要再进行test了，所以改为false
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","false"));    //在return一个jedis实例的时候，是否要进行验证操作，如果赋值为true，则放回jedisPool的实例肯定是可以用的

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    //只需要一个config即可，改为private
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        //连接耗尽的时候，是否阻塞，false抛出异常，true阻塞直到超时，默认为true
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);  //构造器默认也是2秒超时
        //info1.setPassword();      如果redis设置了密码，可以在此添加密码
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
        //本身提供两个分片策略MURMUR_HASH（一致性哈希算法，默认）和MD5
    }

    static{
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis shardedJedis){
        pool.returnResource(shardedJedis);
    }

    public static void returnBrokenResource(ShardedJedis shardedJedis){
        pool.returnBrokenResource(shardedJedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0 ; i < 10 ; i ++){
            jedis.set("key" + i,"value" + i);
        }
        returnBrokenResource(jedis);

//        pool.destroy();//临时调用，销毁连接池中的所有连接，实际业务场景不需要

        System.out.println("program is end");
    }
}
