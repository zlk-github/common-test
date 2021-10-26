##  Rocketmq 介绍

Rocketmq 介绍与常见问题汇总。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-rocketmq-test
    2、公共包github地址：git@github.com:zlk-github/common.git    --(https://github.com/zlk-github/common/tree/master/common-rocketmq)


### 1 Redis 介绍

RocketMQ是一款分布式消息中间件,最初是由阿里消息中间件团队开发，解决线上海量数据堆积问题。2016捐赠到Apache下成为孵化项目。

RocketMQ默认采用长轮询的拉模式，单机可以支持千万级别数据的消息堆积。单机呑吐量理论上接近10W级别。

#### 1.1 MQ 比较（优劣）


#### 1.2 Rocketmq介绍与基本概念

1.2.1 消息模型（Message Model）

    主要由消息生产者（Producer），消息消费者（Consumer ），代理服务器（Broker ）三部分组成。生产者负责生产消息，
    代理服务器负责存储消息，代理服务器在实际部署中对应一台服务器，每个代理服务器可以存放多个主题（Topic）的消息。
    每个主题的消息可以分片存放到不同的代理服务器上。Message Queue 用于存储消息的物理地址，每个主题中的消息中的信息存储于多个Message Queue中。
    消费者组(ConsumerGroup) 由多个消费者实例构成。


#### 1.3 RocketMQ实现原理


#### 1.4 Rocketmq 常见问题

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

### 参考
