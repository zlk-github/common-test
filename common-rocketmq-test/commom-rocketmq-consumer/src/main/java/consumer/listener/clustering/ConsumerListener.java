package consumer.listener.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消费者--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
 @Component
// 消费组rocketmq_group_1001，top为clustering-topic1
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_1,consumerGroup ="${rocketmq.consumer.group1}",messageModel = MessageModel.CLUSTERING)

/*
说明：
nameServer指定mq集群
topic为主题
consumerGroup为消费组（不是生产组），
consumeMode消息类型（ConsumeMode.ORDERLY为顺序消息，默认非顺序）
messageModel消费模式（默认集群消费）--集群消费messageModel = MessageModel.CLUSTERING，广播消费messageModel = MessageModel.BROADCASTING
selectorExpression指定tag过滤条件  --默认全部Topic下tag
@RocketMQMessageListener(nameServer = "127.0.0.1:9877", topic = "test-topic-4", consumerGroup = "my-consumer_test-topic-6",
        consumeMode = ConsumeMode.ORDERLY,messageModel = MessageModel.CLUSTERING)
*/
public class ConsumerListener implements RocketMQListener<Message> {
    @Value("${rocketmq.consumer.group1}")
    private String groupName;

    @Override
    public void onMessage(Message message) {
        log.info("拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.TOPIC_1,message);
    }
}
