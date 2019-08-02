package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {

    public static String set(String key, String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);      //因为发生了异常，因此要返回brokenResource
            return result;  //异常，返回null
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);      //因为发生了异常，因此要返回brokenResource
            return result;  //异常，返回null
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    //exTime单位是秒
    public static String setEx(String key, String value, int exTime){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        }catch (Exception e){
            log.error("setex key:{} value:{} exTime:{} error",key,value,exTime,e);
            RedisPool.returnBrokenResource(jedis);      //因为发生了异常，因此要返回brokenResource
            return result;  //异常，返回null
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    //设置Key的有效期，单位是秒
    public static Long expire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            //设置超时返回1，否则为0
            result = jedis.expire(key, exTime);
        }catch (Exception e){
            log.error("expire key:{} exTime:{} error",key,exTime,e);
            RedisPool.returnBrokenResource(jedis);      //因为发生了异常，因此要返回brokenResource
            return result;  //异常，返回null
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);      //因为发生了异常，因此要返回brokenResource
            return result;  //异常，返回null
        }

        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisPoolUtil.set("keyTest","value");
        String value = RedisPoolUtil.get("keyTest");
        RedisPoolUtil.setEx("keyex","valueex",60*10);
        RedisPoolUtil.expire("keyTest",20*60);
        RedisPoolUtil.del("keyTest");
        System.out.println("end");
    }
}
