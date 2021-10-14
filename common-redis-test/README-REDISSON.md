##  Spring Boot集成Redisson做分布式锁(common-redis-test)

Spring Boot集成Redisson做分布式锁。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-redis-test
    2、公共包github地址：git@github.com:zlk-github/common.git    --(https://github.com/zlk-github/common/tree/master/common-redis)

分布式锁实现与比较

|  技术选型  | 复杂度 <br>（实现成本）  | 性能 | 可靠性 | 优点  | 缺点 | 实现技术 |
|  ----  | ----  |----  | ----  |----  |----  | ----  |
| Redisson分布式锁  | 中 |高 | 中  |  | |基于Redis的setnx与看门狗,即Redisson|
| MySQL分布式锁  | 低 |低 | 低 |  | |基于关系型数据库排他锁 |
| Zookeeper分布式锁  | 高 |中 | 高 |  | |基于Zookeeper |
| Memcached分布式锁  | 中 |高 | 中   |  | |基于Memcached缓存 |

    
    一般情况下Redisson分布式锁是不二之选（大部分项目都会使用到Redis，跨语言API。且实现难度一般，性能高，可靠性也不错）
    
    如果项目中有使用到Zookeeper且团队对Zookeeper熟悉，可以考虑选择Zookeeper分布式锁。

### 1 Redisson分布式锁介绍

    Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。
    默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
    如果程序死亡，到过期时间会自动释放。程序未死亡会自动延时到程序执行完后在程序中自动释放（unlock）。

### 2 Spring Boot集成Redisson做分布式锁

#### 2.2 分布式锁

锁类型介绍见：[锁介绍](https://github.com/zlk-github/general-item/blob/master/src/main/java/com/zlk/jdk/thread/lock/README-LOCK.md#锁介绍)

#### 2.2.1 可重入锁

    基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。

    可重入锁介绍：
        避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)

#### 2.2.2 公平锁

    基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。
    它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。

    公平锁介绍：
        多线程按照申请锁的顺序获取锁。--（先进先出原则为公平锁）

#### 2.2.3 联锁（MultiLock）

    基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。


### 参考

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis
    
    Springboot redis 文档：https://docs.spring.io/spring-data/redis/docs/2.0.3.RELEASE/reference/html/

    Redisson官网 https://redisson.org/

    Redisson官方文档 https://github.com/redisson/redisson/wiki

    Redisson中文文档（完整） https://github.com/redisson/redisson/wiki/目录

    Redisson锁 https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8

    Redisson信号量与闭锁 https://blog.csdn.net/qq_43080270/article/details/113184266?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_utm_term~default-0.no_search_link&spm=1001.2101.3001.4242

    Redisson 使用手册 https://www.bookstack.cn/read/redisson-wiki-zh/Redisson项目介绍.md