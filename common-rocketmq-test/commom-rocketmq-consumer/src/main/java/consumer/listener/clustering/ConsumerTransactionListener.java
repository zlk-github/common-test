package consumer.listener.clustering;


import com.zlk.core.model.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 消费者(事务消息)--集群模式
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_9,consumerGroup ="${rocketmq.consumer.transaction_group9}")
public class ConsumerTransactionListener implements RocketMQListener<Message> {
    // 注：事务是在生产者端做
    @Value("${rocketmq.consumer.transaction_group9}")
    private String groupName;

    @Override
    public void onMessage(Message message) {
        String msg = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        log.info("拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.TOPIC_1,msg);
    }
}
