##  commom-rocketmq-provider

**swagger** : http://localhost:8031/swagger-ui.html

Rocketmq生产者负责把消息推送到Rocketmq供消费者消费。

集群消费模式（Clustering）：1对1发送。
广播消费模式（Broadcasting）：1对多发送方式。

**源码地址见**：

    1、测试项目github地址：https://github.com/zlk-github/common-test/tree/master/common-rocketmq-test/commom-rocketmq-provider
    2、公共包github地址：git@github.com:zlk-github/common.git     --(https://github.com/zlk-github/common)

### 1 Rocketmq 介绍

### 参考

    mq比较https://www.jianshu.com/p/0b1d1fe84e70

    Rocketmq源码(重点)：https://github.com/apache/rocketmq/tree/master/docs/cn
 
    Rocketmq官网：http://rocketmq.apache.org/
    
    Rocketmq文档：http://rocketmq.apache.org/docs/quick-start/

    RocketMQ相关问题：https://blog.csdn.net/QGhurt/article/details/114630705

    集成到springboot2.0 :https://blog.csdn.net/zxl646801924/article/details/105659481/

    注解方式见：http://sharehoo.cn/blog/leg/article/103
              https://blog.csdn.net/weixin_43970890/article/details/116157460
    (详细)https://blog.csdn.net/javahongxi/article/details/86160085

    顺序，重复，消费模式：https://www.cnblogs.com/xuwc/p/9034352.html
