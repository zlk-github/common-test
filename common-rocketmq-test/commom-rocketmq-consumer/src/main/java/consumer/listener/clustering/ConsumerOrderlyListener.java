package consumer.listener.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消费者(顺序消息)--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
@Component
// 消费组rocketmq_group_1001，top为clustering-topic2
@RocketMQMessageListener(topic = RocketMQConstant.CLUSTERING_TOPIC_4,consumerGroup ="${rocketmq.consumer.group4}", consumeMode = ConsumeMode.ORDERLY)
public class ConsumerOrderlyListener implements RocketMQListener<String> {
    @Value("${rocketmq.consumer.group4}")
    private String groupName;

    @Override
    public void onMessage(String message) {
        log.info("顺序消息。拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.CLUSTERING_TOPIC_4,message);
    }


}
