##  Redis集群方案之哨兵模式（一主二从三哨兵）

    Linux：conts7

    Redis版本: 5.0.14
        官网下载网页链接：https://redis.io/download
    
    第三方Redis连接工具推荐：RedisDesktopManager
        官网下载：https://redisdesktop.com/download

### 1 一主二从三哨兵介绍

一主二从三哨兵，1个master主节点，2个slave从节点,对所有3个Redis配置sentinel哨兵模式。
当master节点宕机时，通过哨兵(sentinel)重新推选出新的master节点，保证集群的可用性。

哨兵的主要功能：

    1.集群监控：负责监控 Redis master 和 slave 进程是否正常工作。
    2.消息通知：如果某个 Redis 实例有故障，那么哨兵负责发送消息作为报警通知给管理员。
    3.故障转移：如果 master node 挂掉了，会自动转移到 slave node 上。
    4.配置中心：如果故障转移发生了，通知 client 客户端新的 master 地址。
    PS：根据推举机制，集群中哨兵数量最好为奇数(3、5....)

一主二从三哨兵优缺点：

    优点：能满足高可用，且在满足高可用的情况下使用服务器资源最少（相较于主从与集群模式）
    缺点：但是选举时会间断。扩展难，有性能瓶颈。

### 2 一主二从三哨兵搭建

注：

    资源有限，下面将再同一台机器进行配置测试（开启不同端口Redis服务）。
    （生产环境常规下为一台机器配置一个Redis（容灾高，成本高）。也有按虚拟机部署Redis(容灾差，成本低)）

环境：

|  IP地址  | 端口 | 角色 | Redis版本
|  ----  | ----  |----  | ----  | 
| 47.119.180.152| 6376 |redis-master,sentinel  | 5.0.14 |
| 47.119.180.152| 6377 |redis-slave1,sentinel  | 5.0.14 |
| 47.119.180.152| 6s78 |redis-slave2,sentinel  | 5.0.14 |

Redis安装见：[Linux安装Redis教程](https://github.com/zlk-github/common-test/blob/master/common-redis-test/README-INIT.md#Linux安装Redis教程)

在上面安装的路径copy出三个节点用于测试。

    进入目录： cd /usr/local/redis/redis-5.0.14

    copy一个做主节点（redis-6376）：cp -r /usr/local/redis/redis-5.0.14 /usr/local/redis/redis-6376
    copy一个做从节点1（redis-6377）：cp -r /usr/local/redis/redis-5.0.14 /usr/local/redis/redis-6377
    copy一个做从节点2（redis-6378）：cp -r /usr/local/redis/redis-5.0.14 /usr/local/redis/redis-6378


#### 2.1 配置Redis主节点（redis-6376）

##### 2.1.1 修改Redis配置文件

    vi redis.conf

redis.conf修改内容如下：

```jsx
bind 0.0.0.0            #表示redis允许所有地址连接。默认127.0.0.1，仅允许本地连接。
daemonize yes             #允许redis后台运行
logfile "/var/log/redis_6376.log"    #设置redis日志存放路径
requirepass "123456"        #设置redis密码
protected-mode no      #设置为no，允许外部网络访问
port 6376             #修改redis监听端口(可以自定义)
pidfile /var/run/redis_6376.pid  #pid存放目录
dir /usr/local/redis/redis-6376/tmp   #工作目录，需要创建好目录,可自定义
masterauth 123456    #主从同步master的密码
```
##### 修改Sentinel(哨兵)配置

    vi sentinel.conf

sentinel.conf修改内容如下：

```java
port 2700      #修改Sentinel监听端口
daemonize yes      #允许Sentinel后台运行
logfile "/var/log/redis-sentinel_6376.log"   #设置Sentinel日志存放路径
dir /usr/local/redis/redis-6376/tmp   #工作目录，需要创建好目录,可自定义

#redis01：master名称可自定义
#47.119.180.152 6376 ：redis主节点IP和端口
#2 ：表示多少个Sentinel认为redis主节点失效时，才算真正失效
sentinel monitor redis01 47.119.180.152 6376 2    #Sentinel监听redis主节点

#配置失效时间，master会被这个sentinel主观地认为是不可用的，单位毫秒      
sentinel down-after-milliseconds redis01 10000
        
#若sentinel在该配置值内未能完成master/slave自动切换，则认为本次failover失败。        
sentinel failover-timeout redis01 60000


#在发生failover主备切换时最多可以有多少个slave同时对新的master进行同步。
sentinel parallel-syncs redis01 2

#设置连接master和slave时的密码，注意的是sentinel不能分别为master和slave设置不同的密码，因此master和slave的密码应该设置相同
sentinel auth-pass redis01 123456
```


#### 2.2 配置Redis从节点1（redis-6377）

##### 2.2.1 修改Redis配置文件

    vi redis.conf

redis.conf修改内容如下：

```jsx
bind 0.0.0.0            #表示redis允许所有地址连接。默认127.0.0.1，仅允许本地连接。
daemonize yes             #允许redis后台运行
logfile "/var/log/redis_6377.log"    #设置redis日志存放路径
requirepass "123456"        #设置redis密码
protected-mode no      #设置为no，允许外部网络访问
port 6377             #修改redis监听端口(可以自定义)
pidfile /var/run/redis_6377.pid  #pid存放目录
dir /usr/local/redis/redis-6377/tmp   #工作目录，需要创建好目录,可自定义
masterauth 123456    #主从同步master的密码
replicaof 47.119.180.152 6376 
```
##### 2.2.2 修改Sentinel(哨兵)配置

    vi sentinel.conf

sentinel.conf修改内容如下：

```java
port 2701      #修改Sentinel监听端口
daemonize yes      #允许Sentinel后台运行
logfile "/var/log/redis-sentinel_6377.log"   #设置Sentinel日志存放路径
dir /usr/local/redis/redis-6377/tmp   #工作目录，需要创建好目录,可自定义

#redis01：master名称可自定义
#47.119.180.152 6376 ：redis主节点IP和端口
#2 ：表示多少个Sentinel认为redis主节点失效时，才算真正失效
sentinel monitor redis01 47.119.180.152 6376 2    #Sentinel监听redis主节点

#配置失效时间，master会被这个sentinel主观地认为是不可用的，单位毫秒      
sentinel down-after-milliseconds redis01 10000
        
#若sentinel在该配置值内未能完成master/slave自动切换，则认为本次failover失败。        
sentinel failover-timeout redis01 60000


#在发生failover主备切换时最多可以有多少个slave同时对新的master进行同步。
sentinel parallel-syncs redis01 2

#设置连接master和slave时的密码，注意的是sentinel不能分别为master和slave设置不同的密码，因此master和slave的密码应该设置相同
sentinel auth-pass redis01 123456
```


#### 查看：info replication #查看集群

### 3 Springboot2.0 集成一主二从三哨兵



### 参考

    Redis集群 https://www.cnblogs.com/yufeng218/p/13688582.html

    Redis官网 https://www.redis.net.cn/tutorial/3501.html || https://redis.io/download
    
    Redis源码地址：https://github.com/redis/redis

    查看： https://www.jianshu.com/p/e71c5a3a7162

