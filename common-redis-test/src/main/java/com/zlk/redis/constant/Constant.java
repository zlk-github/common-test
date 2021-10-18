package com.zlk.redis.constant;

/**
 * @author likuan.zhou
 * @title: RedissonLockController
 * @projectName common-test
 * @description: 常量
 * @date 2021/10/18/001 13:40
 */
public interface Constant {
    //布隆过滤器key
    String USER_BLOOM_KEY =  "user:bloomKey";

    //user对应锁key
    String USER_LOCK_KEY =  "user:lock";

    //user对应key
    String USER_KEY =  "user:";
}
