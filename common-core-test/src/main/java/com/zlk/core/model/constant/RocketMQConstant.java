package com.zlk.core.model.constant;

/**
 * RocketMQ常量
 * @author likuan.zhou
 * @date 2021/10/9/009 13:38
 */
public interface RocketMQConstant {

    // 集群模式--（1对1）
    // 主题(一组消息存放在top中，每个服务器可以配置多个TOP)
    String CLUSTERING_TOPIC = "clustering-topic";
    // 主题(一组消息存放在top中，每个服务器可以配置多个TOP)
    String CLUSTERING_TOPIC2 = "clustering-topic2";
    // 标签(同一主题下区分不同类型的消息)
    String CLUSTERING_TAG = "clustering-tag";
    // 标签(同一主题下区分不同类型的消息)
    String CLUSTERING_TAG2 = "clustering-tag2";

}
