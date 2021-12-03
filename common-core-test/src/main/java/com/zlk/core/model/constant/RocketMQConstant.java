package com.zlk.core.model.constant;

/**
 * RocketMQ常量
 * @author likuan.zhou
 * @date 2021/10/9/009 13:38
 */
public interface RocketMQConstant {

    // 集群模式--（1对1）
    // 主题(一组消息存放在top中，每个服务器可以配置多个TOP)
    String TOPIC_1 = "topic1";
    // 主题(一组消息存放在top中，每个服务器可以配置多个TOP)
    String TOPIC_2 = "topic2";
    String TOPIC_3 = "topic3";
    String TOPIC_4 = "topic4";
    String TOPIC_5 = "topic5";
    String TOPIC_6 = "topic6";
    String TOPIC_7 = "topic7";
    String TOPIC_8 = "topic8";
    String TOPIC_9 = "topic9";

    // 标签(同一主题下区分不同类型的消息)
    String TAG_1 = "tag1";
    // 标签(同一主题下区分不同类型的消息)
    String TAG_2 = "tag2";

    String TAG_1_OR_TAG_2 = "tag1||tag2";

}
