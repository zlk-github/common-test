##  Redis介绍与常见问题汇总(common-redis-test)

Redis介绍与常见问题汇总。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git

### 1 Redis 介绍

#### 1.1 Redis为什么快？

redis是用C编写单线程内存型非关系型K-V数据库（单线程仅指Redis负责存取这块的线程只有一个，而非Redis中只有一个进程）。
每秒的QPS理论能达到10W/s。快的原因有以下几点。

    1.采用了多路复用io阻塞机制。
    2.数据结构简单，操作节省时间
    3.redis是C语言编写的，运行在内存中，自然速度快。
        1）.采用单线程，避免了不必要的上下文切换和竞争条件，安全；也不存在多进程或者多线程导致的切换而消耗 CPU，
        不用去考虑各种锁的问题，不存在加锁释放锁操作，没有因为可能出现死锁而导致的性能消耗。
        2）.使用底层模型不同,底层实现方式以及与客户端之间通信的应用协议不一样，Redis直接自己构建了VM 机制 ，因为一般的系统调用系统函数的话，会浪费一定的时间去移动和请求。


#### 1.2 Redis版本差异,单多线程等。

#### 1.3 同类产品比较（优劣）

#### 1.4 Redis的常用5种数据类型：

    string 字符串（可以为整形、浮点型和字符串，统称为元素） --String字符串或者json字符串，常规下使用最多，存放字符串。
           string 类型的值最大能存储 512MB。常用命令：get、set、incr、decr、mget等。

    list 列表（实现队列,元素不唯一，先入先出原则）  -- 类比java中LinkedList，比如twitter的关注列表，粉丝列表等都可以用Redis的list结构来实现。
        “ 双向链表实现”，可以用作栈与队列。内存开销稍大，更新删除等容易，离链表两端远查询会稍慢。
         常用命令：lpush（添加左边元素）,rpush,lpop（移除左边第一个元素）,rpop,lrange（获取列表片段，LRANGE key start stop）等。

    set 集合（各不相同的元素） -- 类比java中HashSet，redis中set集合是通过hashtable实现的，没有顺序，适合存放需要去重的数据。
        常用命令：sadd,spop,smembers,sunion 等。

    hash hash散列值（hash的key必须是唯一的） -- 类比java中HashMap (list转换后的Map; 一般为key:map<item,Object>,其中item为Object对象主键) ，
        适合存放转换后对象列表（通过id查询对应数据，如用户信息）。常用命令：hget,hset,hgetall 等。
   
    sort set 有序集合  -- 类比java中HashSet，但是有分值比重，适合做去重的排行榜等热数据。“数据结构是跳跃表”
         常用命令：zadd,zrange,zrem,zcard等

    另：范围查询，Bitmaps，Hyperloglogs 和地理空间（Geospatial）索引半径查询

#### 1.5 Redis 常见问题

##### 1.5.1 Redis为什么是单线程的？

    因为Redis的瓶颈不是cpu的运行速度，而往往是网络带宽和机器的内存大小。再说了，单线程切换开销小，容易实现既然单线程容易实现，而且CPU不会成为瓶颈，那就顺理成章地采用单线程的方案了。（单线程仅指Redis负责存取这块的线程只有一个，而非Redis中只有一个进程）

##### 1.5.2 简述Redis的数据淘汰机制

    volatile-lru 从已设置过期时间的数据集中挑选最近最少使用的数据淘汰
    volatile-ttl 从已设置过期时间的数据集中挑选将要过期的数据淘汰
    volatile-random从已设置过期时间的数据集中任意选择数据淘汰
    allkeys-lru从所有数据集中挑选最近最少使用的数据淘汰
    allkeys-random从所有数据集中任意选择数据进行淘汰
    noeviction禁止驱逐数据

##### 1.5.3 Redis持久化

Redis怎样防止异常数据不丢失

详见：* [Redis持久化](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-PERSISTENCE.md#Redis持久化)

    RDB 持久化 （快照的形式，周期性的持久化）** 是Redis的默认持久化方法 适合冷备
    AOF 持久化 （追加到AOF文件的末尾，最后以不同的频次保存到到磁盘）适合热备
        appendonly yes
        appendfilename "appendonly.aof"
        appendfsync everysec

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
