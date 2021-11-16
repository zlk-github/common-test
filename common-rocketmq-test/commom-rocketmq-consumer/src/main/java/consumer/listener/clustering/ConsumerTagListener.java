package consumer.listener.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消费者(tag过滤消息)--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
@Component
// 消费组rocketmq_group_1007，top为clustering-topic7
@RocketMQMessageListener(topic = RocketMQConstant.CLUSTERING_TOPIC_7,consumerGroup ="${rocketmq.consumer.group7}")

public class ConsumerTagListener implements RocketMQListener<MessageExt> , RocketMQPushConsumerLifecycleListener {
    @Value("${rocketmq.consumer.group1}")
    private String groupName;

    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody());
        log.info("tag过滤消息。拿到消费组：{}，主题Top:{}下消息,消息：{},tag:{}",groupName,RocketMQConstant.CLUSTERING_TOPIC_7,message,messageExt.getTags());
    }

    @SneakyThrows
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 过滤获取到RocketMQConstant.CLUSTERING_TOPIC_7下tag:RocketMQConstant.TAG_1
        defaultMQPushConsumer.subscribe(RocketMQConstant.CLUSTERING_TOPIC_7,RocketMQConstant.TAG_1);
    }
}
