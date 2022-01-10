##  Rocketmq 介绍

Rocketmq 介绍与常见问题汇总。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-rocketmq-test
    2、公共包github地址：git@github.com:zlk-github/common.git    --(https://github.com/zlk-github/common/tree/master/common-rocketmq)

### 1 高可用实现与数据同步

   参考：https://www.cnblogs.com/xwgblog/p/14055449.html

#### 1.1 Broker 集群部署架构

    1. 多主模式
      集群无Slave，全是Maste。
      优点：配置简单，单个Master宕机或重启维护对应用无影响，在磁盘配置为RAID10 时，即使机器宕机不可恢复情
      由于RAID10 磁盘非常可靠，消息也不会丢（异步刷盘丢失少量消息，同步刷盘一条不丢）。性能最高。
      缺点：单台机器宕机期间，这台机器上未被消费的消息在机器恢复之前不可订阅，消息实时性会受到受到影响。

    2. 多主多从模式-异步复制 模式
      每个Master配置一个Slave，有多对Master-Slave，HA采用异步复制方式，主备有短暂消息延迟（毫秒级）
      优点：即使磁盘损坏，消息丢失的非常少，且消息实时性不会受影响，同时Master宕机后，消费者仍然可以从Slave消费，而且此过程对应用透明，不需要人工干预，性能同多Master模式几乎一样；
      缺点：Master宕机，磁盘损坏情况下会丢失少量消息。
            
    3 多主多从模式-同步双写 模式
      每个Master配置一个Slave，有多对Master-Slave，HA采用同步双写方式，即只有主备都写成功，才向应用返回成功
      优点：数据与服务都无单点故障，Master宕机情况下，消息无延迟，服务可用性与数据可用性都非常高；
      缺点：性能比异步复制模式略低（大约低10%左右），发送单个消息的RT会略高，且目前版本在主节点宕机后，备机不能自动切换为主机。

    4 Dledger部署（优先)
      RocketMQ 4.5 以前的版本大多都是采用 Master-Slave 架构来部署，能在一定程度上保证数据的不丢失，也能保证一定的可用性。
      但是那种方式 的缺陷很明显，最大的问题就是当 Master Broker 挂了之后 ，没办法让 Slave Broker 自动 切换为新的 Master Broker，
      需要手动更改配置将 Slave Broker 设置为 Master Broker，以及重启机器，这个非常麻烦。
  
      在手式运维的期间，可能会导致系统的不可用。
  
      使用 Dledger 技术要求至少由三个 Broker 组成 ，一个 Master 和两个 Slave，这样三个 Broker 就可以组成一个 Group ，
      也就是三个 Broker 可以分组来运行。一但 Master 宕机，Dledger 就可以从剩下的两个 Broker 中选举一个 Master 继续对外提供服务。

    注：
      主从模式：主从节点数据一致，各主节点数据分区分布（不一样）。
      Dledger集群：

### 2 高可用实现之双主模式



### 参考
