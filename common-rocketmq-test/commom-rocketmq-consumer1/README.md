##  commom-rocketmq-consumer1(不在维护)

rocketmq消费者，负责消费rocketmq中对应的mq消息。有消费者服务主动拉取（拉取式消费）与rocketmq自动推送（推动式消费）到消费者服务两种方式。

集群消费模式（Clustering）：消息会被消费者服务平均分摊。
广播消费模式（Broadcasting）：相同的消费者组都会接收到全量的消息。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-rocketmq-test/commom-rocketmq-consumer
    2、公共包github地址：git@github.com:zlk-github/common.git     --(https://github.com/zlk-github/common)

### 1 Rocketmq 介绍

### 参考


    mq比较https://www.jianshu.com/p/0b1d1fe84e70

    Rocketmq源码(重点)：https://github.com/apache/rocketmq/tree/master/docs/cn
 
    Rocketmq官网：http://rocketmq.apache.org/
    
    Rocketmq文档：rocketmq文档地址：http://rocketmq.apache.org/docs/quick-start/

    RocketMQ相关问题：https://blog.csdn.net/QGhurt/article/details/114630705
