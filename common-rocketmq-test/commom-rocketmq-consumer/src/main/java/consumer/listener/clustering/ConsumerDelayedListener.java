package consumer.listener.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

/**
 * 消费者(延时消息)--集群模式
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
@Slf4j
//@Component
// 消费组rocketmq_group_1003，top为clustering-topic3
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_3,consumerGroup ="${rocketmq.consumer.group3}")
public class ConsumerDelayedListener implements RocketMQListener<MessageExt> {
    @Value("${rocketmq.consumer.group3}")
    private String groupName;

    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String((byte[]) messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("延时消息，拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.TOPIC_3,message);
        log.info("延时时间：{}",System.currentTimeMillis()-messageExt.getBornTimestamp());
    }
}
