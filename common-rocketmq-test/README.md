##  common-rocketmq-test

Spring Boot 如何集成RocketMQ做消息队列

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-rocketmq-test
    2、公共包github地址：git@github.com:zlk-github/common.git     --(https://github.com/zlk-github/common)

### 1 RocketMQ 介绍

详见：* [RocketMQ介绍与常见问题汇总](https://github.com/zlk-github/common-test/blob/master/common-rocketmq-test/README-INTRODUCE.md#Rocketmq介绍与常见问题汇总)

1.1 MQ 比较（优劣）

1.2 RocketMQ介绍与基本概念

1.3 RocketMQ 常见问题

    Rocketmq 使用场景
    RocketMQ消费模式有几种
    RocketMQ基本概念
    消息异步情况下的可靠性传输
        生产者丢失：消息重试（ACK+失败入库。事务开启。防止发送失败）
        MQ丢失：数据防止丢失（消息、队列、交换器等持久化。集群部署）
        消费者丢失：数据消费丢失（手动ACK）
        消费失败大量积压的数据怎么解决，积压到磁盘上线，数据被删除了怎么办。
    RocketMQ为什么速度快
    RocketMQ事务
    重复消费和幂等性问题
    如何保证消息的顺序
    高可用实现与数据同步
    RocketMQ实现原理
    
另外：
    
    生产者一条发送rocketmq存在两条数据
    消费结束数据还存在于rocketmq


安装见：[Linux安装RocketMQ教程](https://github.com/zlk-github/common-test/blob/master/common-rocketmq-test/README-INIT.md#Linux安装Rocketmq教程)


### 2 Spring Boot 集成RRocketMQ做消息队列

主要测试消息发送与消费，并解决如下问题。
  
    RocketMQ基本概念
    Rocketmq 使用场景
    RocketMQ生产消费模式有几种
    消息异步情况下的可靠性传输
        生产者丢失：消息重试（ACK+失败入库。事务开启。防止发送失败）
        MQ丢失：数据防止丢失（消息、队列、交换器等持久化。集群部署）
        消费者丢失：数据消费丢失（手动ACK）
        消费失败大量积压的数据怎么解决，积压到磁盘上线，数据被删除了怎么办。
    RocketMQ为什么速度快
    RocketMQ事务
    重复消费和幂等性问题
    如何保证消息的顺序
    高可用实现与数据同步
    RocketMQ实现原理

### 4 RocketMQ实现原理

### 3 RocketMQ集群方案（高可用）

### 3 RocketMQ性能指标与优化方案

#### 4 RocketMQ 常用命令

详见： [Rocketmq常用命令](https://github.com/zlk-github/common-test/blob/master/common-rocketmq-test/README-COMMAND.md#Rocketmq常用命令)


### 参考

    mq比较https://www.jianshu.com/p/0b1d1fe84e70

    RocketMQ源码(重点)：https://github.com/apache/rocketmq/tree/master/docs/cn
 
    RocketMQ官网：http://rocketmq.apache.org/
    
    RocketMQ文档：rocketmq文档地址：http://rocketmq.apache.org/docs/quick-start/

    RocketMQ相关问题：https://blog.csdn.net/QGhurt/article/details/114630705
