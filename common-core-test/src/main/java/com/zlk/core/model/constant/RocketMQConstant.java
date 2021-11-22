package com.zlk.core.model.constant;

/**
 * RocketMQ常量
 * @author likuan.zhou
 * @date 2021/10/9/009 13:38
 */
public interface RocketMQConstant {

    // 集群模式--（1对1）
    // 主题(一组消息存放在top中，每个服务器可以配置多个TOP)
    String CLUSTERING_TOPIC_1 = "clustering-topic1";
    // 主题(一组消息存放在top中，每个服务器可以配置多个TOP)
    String CLUSTERING_TOPIC_2 = "clustering-topic2";
    String CLUSTERING_TOPIC_3 = "clustering-topic3";
    String CLUSTERING_TOPIC_4 = "clustering-topic4";
    String CLUSTERING_TOPIC_5 = "clustering-topic5";
    String CLUSTERING_TOPIC_6 = "clustering-topic6";
    String CLUSTERING_TOPIC_7 = "clustering-topic7";
    String CLUSTERING_TOPIC_8 = "clustering-topic8";
    String CLUSTERING_TOPIC_9 = "clustering-topic9";

    // 标签(同一主题下区分不同类型的消息)
    String TAG_1 = "clustering-tag1";
    // 标签(同一主题下区分不同类型的消息)
    String TAG_2 = "clustering-tag2";

}
