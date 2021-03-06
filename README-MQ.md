## 消息队列MQ介绍与比较

消息队列主要作用：应用解耦，异步通信，流量削峰，数据同步（数据最终一致性）。

    生产者: 发送消息
    MQ: 做消息中转
    消费者：消费消息

使用场景：邮件推送，短信通知，日志收集处理（主要是kafka）,耗时长请求多且不要求实时返回结果的业务。

### 1 ActiveMQ(实际中使用少)

社区活跃度不及RabbitMQ高,可能会出现消息丢失.

### 2 RabbitMQ

### 3 ZeroMQ

### 4 RocketMQ

### 5 Kafka

### 6 MQ 比较

    特性	      ActiveMQ	RabbitMQ	Kafka	RocketMQ
    PRODUCER-COMSUMER	支持	支持	支持	支持
    PUBLISH-SUBSCRIBE	支持	支持	支持	支持
    REQUEST-REPLY	支持	支持	-	支持
    API完备性	高	高	高	低（静态配置）
    多语言支持	支持，JAVA优先	语言无关	支持，JAVA优先	支持
    单机呑吐量	万级	万级	十万级（吞吐量高）	十万级
    消息延迟  	毫秒级	微秒级	毫秒级以内	 毫秒级
    可用性	高（主从）	高（主从）	非常高（分布式）	高（分布式架构）
    消息丢失	        -	低	理论上不会丢失	-
    消息重复	        -	可控制	理论上会有重复	-
    文档的完备性	高	高	高	中
    提供快速入门	有	有	有	无
    首次部署难度	-	低	中	高
    开发语言	java	erlang	scala	java
    可用性	高(主从架构)	高(主从架构)	非常高(分布式架构)	非常高(分布式架构)
    功能特性	成熟的产品，在很多公司得到应用；有较多的文档；各种协议支持较好||	基于erlang开发，所以并发能力很强，性能极其好，延时很低;管理界面较丰富||	只支持主要的MQ功能，像一些消息查询，消息回溯等功能没有提供，毕竟是为大数据准备的，在大数据领域应用广。||MQ功能比较完备，扩展性佳
    事务支持
    持久化	内存、文件、数据库||	内存、文件，支持数据堆积，但数据堆积会影响生产速率||		磁盘文件，只要磁盘容量足够，可以做到无限消息堆积||磁盘文件
    功能	成熟的社区产品、文档丰富 || 并发强、性能好、延时低	|| 顺序消息、事务消息等功能完善 || 只支持主要的MQ功能（MQ只是其中一个功能）	


|  **特性**  | **ActiveMQ** | **RabiitMQ** | **RocketMQ**| **Kafka**
|  ----  | ----  |----  | ----  | ----  |
| 集群| 高(主从) |高（主从） | 非常高（分布式） | 非常高（分布式） |
| 单机呑吐量| 万级 |万级 | 十万级（吞吐量高) |十万级 |
| 消息延迟| 毫秒级|微秒级 | 毫秒级 |毫秒级以内 |
| 失败重试| 支持 |支持| 支持 |不支持|
| 消息顺序消费| 不支持 |支持单分区级别顺序消费 | 支持 |不支持 |
| 延时消息| 支持 |不支持(需要插件等支持) | 支持 |不支持 |
| 分布式事务消息| 支持 |不支持 | 支持 |不支持 |
| 消息堆积| 支持 |支持 | 支持 |支持（到阈值会影响性能） |
| 持久化| 内存，磁盘文件，数据库 |磁盘文件 | 磁盘文件 |磁盘文件 |
| 消息丢失| 低| 低| 理论上不会丢失|理论上不会丢失|
| 消息重复| | 可控制| |理论上会有重复|
| API文档完备性| 高 |高 | 低（静态配置） |高 |
| 社区活跃度| 一般 |高 | 高 |高 |
| github活跃度(2021-12-23)| *2K stars|*9.1k stars| *16.2k stars|*20.7k stars|
| 开源情况| 开源 |开源 | 开源（部分） |开源 |
| 开发语言| java |erlang |java |scala|
| 支持客户端| java,C++,go等 |java,C++,go等   | java,C++,go等   |java,C++,go等  |
| 使用选择| 使用较少见 |消息实时性要求高 |事务消息，吞吐量高 |实时计算、日志采集|

MQ选择：
1.ActiveMQ吞吐量问题加上社区不活跃，现在基本很少有项目使用ActiveMQ。
2.RabbitMQ实时性高，项目开源稳定，但是erlang语言阻止了大量java工程师的深入研究。
  RocketMQ是java编写，吞吐量优于RabbitMQ，支持事务消息，但是需要承担社区黄调掉的风险。
  中小型公司，技术实力一般推荐RabbitMQ。大型公司，基础架构研发实力较强，用RocketMQ是很好的选择。
3.如果是大数据领域的实时计算、日志采集等场景，用Kafka是业内标准的，绝对没问题，社区活跃度很高，绝对不会黄。


### MQ常见问题

    高可用实现。
    消息重试（ACK+失败入库。事务开启。防止发送失败）。
    数据防止丢失（消息、队列、交换器等持久化。集群部署）。
    重复消费和幂等性问题。
    数据消费丢失（手动ACK）。
    如何保证消息的顺序。


### 参考

    mq比较https://www.jianshu.com/p/0b1d1fe84e70  || http://rocketmq.apache.org/docs/motivation/

    rocketmq源码：https://github.com/apache/rocketmq/tree/master/docs/cn
    
    rocketmq文档：rocketmq文档地址：http://rocketmq.apache.org/docs/quick-start/

