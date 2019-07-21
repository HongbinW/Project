package com.mmall.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author: HongbinW
 * @Date: 2019/4/9 14:15
 * @Version 1.0
 * @Description:
 */
@Slf4j
public class TokenCache {

//    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);//日志

    public static final String TOKEN_PREFIX = "token_";

    //静态内存块
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值的时候，如果Key没有对应的值，就会调用这个方法进行加载。
                @Override
                public String load(String s) throws Exception {
                    return "null";  //换成字符串的Null,是为了防止调用的时候.equals()，会报空指针
                }
            });
    //initialCapacity(1000)，设置缓存的初始化容量为1000
    //maximumSize(10000)，缓存的最大容量。   如果超过10000，会使用LRU算法
    //expireAfterAccess(12,TimeUnit.HOURS)缓存有效期为12小时

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            log.error("localCache get error",e);
        }
        return null;
    }

}
