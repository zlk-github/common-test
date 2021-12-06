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

1.2.2 消息生产者（Producer）

    负责生产消息发送到broker服务器。提供同步消息、异步消息、顺序消息、单向消息、事务消息、过滤消息与批量消息等。
    其中同步和异步方式均需要Broker返回确认信息，单向发送不需要（不能保证必达，效率高）。

1.2.3 消息消费者（Consumer）

    负责消费broker服务器中的消息，从MQ拉取消息进行消费。
    提供两种消费形式：拉取式消费、推送式消费

1.2.4 主题（Topic）

    一类消息的集合，每个主题包含若干条消息，每条消息只能属于一个主题，是RocketMQ进行消息订阅的基本单位。

1.2.5 代理服务器（Broker Server）

    消息中转角色，负责存储与转发消息。
    代理服务器也存储消息相关的元数据，包括消费者组、消费进度偏移和主题和队列消息等。

    Broker部署相对复杂，Broker分为Master与Slave，一个Master可以对应多个Slave，但是一个Slave只能对应一个Master，
    Master与Slave 的对应关系通过指定相同的BrokerName，不同的BrokerId 来定义，BrokerId为0表示Master，非0表示Slave。Master也可以部署多个。
    每个Broker与NameServer集群中的所有节点建立长连接，定时注册Topic信息到所有NameServer。 
    注意：当前RocketMQ版本在部署架构上支持一Master多Slave，但只有BrokerId=1的从服务器才会参与消息的读负载。

1.2.6 名字服务（Name Server）

    名称服务充当路由消息的提供者。生产者或消费者能够通过名字服务查找各主题相应的Broker IP列表。
    多个Namesrv实例组成集群，但相互独立，没有信息交换。

1.2.7 拉取式消费（Pull Consumer）

    一种消费类型，消费者主动从Broker服务器拉取消息进行消费。批量获取消息。

1.2.8 推动式消费（Push Consumer）

    一种消费类型，Broker服务器收到数据后会主动推送给消费端，该消费模式一般实时性较高。

1.2.9 生产者组（Producer Group）

    同一类生产者的集合，如果发送事务消息后原生产者崩溃，Broker服务器可以联系同一类生产者组的其他生产者做提交与回溯消费（事务提交与回滚）。

1.2.10 消费者组（Consumer Group）

    同一类消费者的集合，用于负载均衡与容错。消费者必须订阅相同的topic。消费模式：集群消费与广播消费。

1.2.11 集群消费（Clustering）|| 广播消费（Broadcasting）

    集群消费模式下,相同Consumer Group的每个Consumer实例平均分摊消息。不同的消费组也是全量消息。
    广播消费模式下，相同Consumer Group的每个Consumer实例都接收全量的消息。（少见）

1.2.12 普通顺序消息（Normal Ordered Message）|| 严格顺序消息（Strictly Ordered Message）

    普通顺序消费模式下，消费者通过同一个消息队列（ Topic 分区，称作 Message Queue） 收到的消息是有顺序的，不同消息队列收到的消息则可能是无顺序的。
    严格顺序消息模式下，消费者收到的所有消息均是有顺序的。

1.2.13 消息（Message）

    信息的载体，生产与消费的最小单位，消息必须属于一个Topic。
    RocketMQ中每个消息拥有唯一的Message ID（存在问题，不要作为业务去重），且可以携带具有业务标识的Key（顺序消息与幂等使用）。
    系统提供了通过Message ID和Key查询消息的功能。

1.2.14 标签（Tag）

    topic的一个补充，一个topic下可以有不同类型的消息。可以根据业务设置不同类型的标签，消费者可以根据标签实现不同的消费逻辑拓展。

#### 1.3 RocketMQ实现原理

#### 1.4 Rocketmq 常见问题

    Rocketmq 使用场景
    RocketMQ消费模式有几种
    消息异步情况下的可靠性传输
        生产者丢失：消息重试，RocketMQ事务，定时任务补偿(对前两个的补充)。
        MQ丢失：数据同步刷新（使用少），MQ集群备份。
        消费者丢失：消息消费业务结束再回复状态给MQ。
    消费失败大量积压的数据怎么解决，积压到磁盘上线，数据被删除了怎么办。
    RocketMQ为什么速度快
    重复消费和幂等性问题
    高可用实现与数据同步（见1.3）
    RocketMQ实现原理（见1.5）

##### 1.4.1 Rocketmq 使用场景
 
    异步，流量削锋，解耦。

    异步消息：如短信、邮件、日志（日志一般用elk）等，缩短接口响应速度。
    流量削锋：下单秒杀等,先进入MQ排队，增加消费者消费，避免压垮服务与数据库。
    解耦：和第三方平台解耦，做中间数据交互，达到跨语言跨系统交互。

##### 1.4.2 RocketMQ为什么速度快

##### 1.4.3 RocketMQ消费模式有几种
 
    集群消费：启动多个消费者服务，同一消费者组平均消费分摊消息（同一消费者消费同一个topic下消息）
    广播消费：启动多个消费者服务，同一消费者组全量消费消息（同一消费者消费同一个topic下消息）

##### 1.4.4 RocketMQ消息可靠传输（0丢失）

![Image text](./images/消息丢失场景.png)

![Image text](./images/消息丢失解决方案.png)


##### 1.4.5 RocketMQ消费失败大量积压与消息被删除的解决

##### 1.4.6 RocketMQ消息之顺序消息

    RocketMQ消息有同步消息、异步消息、顺序消息、单向消息、事务消息、过滤消息与批量消息等。以下介绍顺序消息。

##### 1.4.7 RocketMQ消费者之重复消费和幂等性问题

#### 1.3 高可用实现与数据同步

### 参考
