package consumer.listener.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消费者(消费失败重试)--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
@Component
// 消费组rocketmq_group_1005，top为clustering-topic5
@RocketMQMessageListener(topic = RocketMQConstant.CLUSTERING_TOPIC_5,consumerGroup ="${rocketmq.consumer.group5}",consumeMode = ConsumeMode.ORDERLY)
public class ConsumerRetryListener implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
    @Value("${rocketmq.consumer.group5}")
    private String groupName;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            log.info("自定义消息体。拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.CLUSTERING_TOPIC_5,messageExt.getBody());
            int s = 1 / 0;
            log.info("消息重试！！！！！！");
        } catch (Exception ex) {
            // 1需要重试,把异常抛出去。最后还失败，消息会留在MQ。
            // TODO 待完善，需要丢到死信队列或者数据库，做人工干预等。
            // throw ex;

            //2不需要重试则默认把异常捕获掉，默认该消息已被消费，mq里面会剔除(去掉异常抛出)
            //log.info("不需要重试则默认把异常捕获掉，默认该消息已被消费，mq里面会剔除",ex);
        }
    }


    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 设置最大重试次数
        defaultMQPushConsumer.setMaxReconsumeTimes(5);
        // 如下，设置其它consumer相关属性
        defaultMQPushConsumer.setPullBatchSize(16);
    }
}
