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

import java.nio.charset.StandardCharsets;

/**
 * 消费者(全量tag过滤消息)--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 *     注：
 *     tag模式下需要集群消费需要使用不同的消费组（类似于同一个消费组的广播消费模式），否则存在交叉的tag消息也会被均分导致少消费数据，如不存在交叉tag则不会存在该问题。
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
@Component
// 消费组rocketmq_group_1007，top为clustering-topic7
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_7,consumerGroup ="${rocketmq.consumer.group7_3}")
public class ConsumerTagListener3 implements RocketMQListener<MessageExt> , RocketMQPushConsumerLifecycleListener {
    @Value("${rocketmq.consumer.group1}")
    private String groupName;

    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String((byte[]) messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("tag(全部)过滤消息。拿到消费组：{}，consumerGroup：{}，主题Top:{}下消息,tag:{},消息：{}","rocketmq_consumer_group_1007_3",groupName,RocketMQConstant.TOPIC_7,messageExt.getTags(),message);
    }

    @SneakyThrows
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 过滤获取到RocketMQConstant.TOPIC_7-3下tag:"*"
        //defaultMQPushConsumer.subscribe(RocketMQConstant.TOPIC_7_3,"*");
        // 设置最大重试次数
        defaultMQPushConsumer.setMaxReconsumeTimes(5);
        // 如下，设置其它consumer相关属性
        defaultMQPushConsumer.setPullBatchSize(16);
    }

}
