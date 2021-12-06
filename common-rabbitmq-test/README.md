##  common-rabbitmq-test

Spring Boot 如何集成rabbitmq做消息队列

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-rabbitmq-test
    2、公共包github地址：git@github.com:zlk-github/common.git     --(https://github.com/zlk-github/common)

### 1 rabbitmq 介绍

详见：* [rabbitmq介绍与常见问题汇总](https://github.com/zlk-github/common-test/blob/master/common-rabbitmq-test/README-INTRODUCE.md#rabbitmq介绍与常见问题汇总)

1.1 MQ 比较（优劣）


1.2 rabbitmq介绍与基本概念

1.3 rabbitmq 常见问题

    rabbitmq 使用场景
    rabbitmq消费模式有几种
    rabbitmq基本概念
    消息异步情况下的可靠性传输
        生产者丢失：消息重试（ACK+失败入库。事务开启。防止发送失败）
        MQ丢失：数据防止丢失（消息、队列、交换器等持久化。集群部署）
        消费者丢失：数据消费丢失（手动ACK）
        消费失败大量积压的数据怎么解决，积压到磁盘上线，数据被删除了怎么办。
    rabbitmq为什么速度快
    rabbitmq事务
    重复消费和幂等性问题
    如何保证消息的顺序
    高可用实现与数据同步
    rabbitmq实现原理
    
另外：
    
    生产者一条发送rabbitmq存在两条数据
    消费结束数据还存在于rabbitmq


安装见：[Linux安装rabbitmq教程](https://github.com/zlk-github/common-test/blob/master/common-rabbitmq-test/README-INIT.md#Linux安装rabbitmq教程)


### 2 Spring Boot 集成Rrabbitmq做消息队列

主要测试消息发送与消费，并解决如下问题。
  
    rabbitmq基本概念
    rabbitmq 使用场景
    rabbitmq生产消费模式有几种
    消息异步情况下的可靠性传输
        生产者丢失：消息重试（ACK+失败入库。事务开启。防止发送失败）
        MQ丢失：数据防止丢失（消息、队列、交换器等持久化。集群部署）
        消费者丢失：数据消费丢失（手动ACK）
        消费失败大量积压的数据怎么解决，积压到磁盘上线，数据被删除了怎么办。
    rabbitmq为什么速度快
    rabbitmq事务
    重复消费和幂等性问题
    如何保证消息的顺序
    高可用实现与数据同步
    rabbitmq实现原理

### 4 rabbitmq实现原理

### 3 rabbitmq集群方案（高可用）

### 3 rabbitmq性能指标与优化方案

#### 4 rabbitmq 常用命令

详见： [rabbitmq常用命令](https://github.com/zlk-github/common-test/blob/master/common-rabbitmq-test/README-COMMAND.md#rabbitmq常用命令)


### 参考

    mq比较https://www.jianshu.com/p/0b1d1fe84e70

    rabbitmq源码(重点)：https://github.com/apache/rabbitmq/tree/master/docs/cn
 
    rabbitmq官网：http://rabbitmq.apache.org/
    
    rabbitmq文档：rabbitmq文档地址：http://rabbitmq.apache.org/docs/quick-start/

    rabbitmq相关问题：https://blog.csdn.net/QGhurt/article/details/114630705

    参考博客： https://www.cnblogs.com/xuwc/p/9034352.html

    面试题： https://blog.csdn.net/xiaotai1234/article/details/119117747
