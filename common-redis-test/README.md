##  common-redis-test

Spring Boot 如何集成redis做缓存(默认过期时间),分布式锁，布隆表达式，消息传递/发布订阅, Redis 事务，redis持久化，redis集群方案等。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git     --(https://github.com/zlk-github/common)

### 1 Redis 介绍

详见：* [Redis介绍与常见问题汇总](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-INTRODUCE.md#Redis介绍与常见问题汇总)

    1.1 Redis为什么快
    1.2 redis版本差异,单多线程等。
    1.3 同类产品比较（优劣）
    1.4 Redis的常用5种数据类型与选择
    1.5 Redis 常见问题
        1.5.1 什么场景适合使用缓存
        1.5.2 Redis为什么是单线程的
        1.5.3 简述Redis的数据淘汰机制
        1.5.4 Redis怎样防止异常数据不丢失

### 2 Spring Boot 集成redis做缓存

详见：* [Spring Boot集成redis做缓存](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-CACHE.md#SpringBoot集成redis做缓存)

    Spring Boot 集成redis缓存工具类。与项目中的缓存使用。

### 3 Spring Boot 集成Redission做分布式锁

详见：* [Spring Boot集成Redission做分布式锁](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-REDISSION.md#SpringBoot集成Redission做分布式锁)

### 4 Redis穿透，击穿，雪崩
    
    Redis缓存穿透：数据库与缓存中都不存在，黑客大量访问打到数据库；（布隆过滤器/返回空对象）
    Redis缓存击穿：数据库中存在对应值，redi缓存过期，大量请求访问打到数据库；（分布式锁Redisson）
    Redis缓存雪崩：缓存大面积失效，或者重启消耗大量资源。（缓存时间设置随机，启动加到队列,冷热数据分离，预加载等，热数据均匀分布到不同缓存数据库）

### 5 消息传递/发布订阅

### 6 Redis 事务

### 7 Redis持久化

### 8 Redis集群方案

#### 8.1 主从-哨兵模式

一主两从三哨兵。能满足高可用，但是选举时会间断。扩展难，有性能瓶颈。

#### 8.2 集群模式

最少3个master节点，且每个主节点下挂一个slave节点。

### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis
    
    Rpringboot redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    Redis速度为什么这么快 https://blog.csdn.net/xlgen157387/article/details/79470556

    Redisson官网 https://redisson.org/

    Redisson官方文档 https://github.com/redisson/redisson/wiki

    Redisson中文文档（完整） https://github.com/redisson/redisson/wiki/目录

    Redis介绍：https://blog.csdn.net/qq_44472134/article/details/104252693
    
    Redis数据结构： https://zhuanlan.zhihu.com/p/344918922
    
    Redis跳跃表：https://www.jianshu.com/p/c2841d65df4c

    Redis跳跃表（详细）： https://www.jianshu.com/p/c2841d65df4c

    Redisson锁 https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8

    Redisson信号量与闭锁 https://blog.csdn.net/qq_43080270/article/details/113184266?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_utm_term~default-0.no_search_link&spm=1001.2101.3001.4242

