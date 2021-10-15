package com.zlk.redis.constant;

/**
 * @author likuan.zhou
 * @title: RedissonConstant
 * @projectName common-test
 * @description: Redisson常量
 * @date 2021/10/9/009 13:38
 */
public interface RedissonConstant {
    /**过期时间*/
     Long EXPIRE_TIME = 60*60L;

    //kye分割使用:会按文件夹方式分割归类。
    /**字符串类型key（key:String）*/
    String LOCK_KEY = "lock:key";
}
