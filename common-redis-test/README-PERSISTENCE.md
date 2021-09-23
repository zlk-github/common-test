##  (Redis持久化) common-redis-test

Redis持久化。

### 1 Redis持久化

详见：* [redis持久化](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-PERSISTENCE.md#redis持久化)

    RDB 持久化 （快照的形式，周期性的持久化）** 是Redis的默认持久化方法 适合冷备
    AOF 持久化 （追加到AOF文件的末尾，最后以不同的频次保存到到磁盘）适合热备
        appendonly yes
        appendfilename "appendonly.aof"
        appendfsync everysec

### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    redis源码地址：https://github.com/redis/redis
    
    springboot redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556

    redisson官网 https://redisson.org/

    redisson官方文档 https://github.com/redisson/redisson/wiki

    redisson中文文档（完整） https://github.com/redisson/redisson/wiki/目录

    redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693

