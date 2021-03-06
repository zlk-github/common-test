package consumer.listener.broadcasting;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

/**
 * 消费者(全量tag过滤消息)--广播模式（1对多，相同消费组全量接收消息）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
//@Component
// 消费组rocketmq_group_1007，top为clustering-topic7
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_7,consumerGroup ="${rocketmq.consumer.group7_6}",selectorExpression = RocketMQConstant.TAG_1_OR_TAG_2,messageModel = MessageModel.BROADCASTING)
public class ConsumerTagListener5 implements RocketMQListener<MessageExt> , RocketMQPushConsumerLifecycleListener {
    @Value("${rocketmq.consumer.group7_6}")
    private String groupName;




    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String((byte[]) messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("广播模式--》tag(tag2||tag2)过滤消息。拿到消费组：{}，consumerGroup：{}，主题Top:{}下消息,tag:{},消息：{}","rocketmq_consumer_group_1007_5",groupName,RocketMQConstant.TOPIC_7,messageExt.getTags(),message);
    }

    @SneakyThrows
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 过滤获取到RocketMQConstant.TOPIC_7-4下tag:(tag2||tag2)
        //defaultMQPushConsumer.subscribe(RocketMQConstant.TOPIC_7_5, RocketMQConstant.TAG_1_OR_TAG_2);
        // 设置最大重试次数
        defaultMQPushConsumer.setMaxReconsumeTimes(5);
        // 如下，设置其它consumer相关属性
        defaultMQPushConsumer.setPullBatchSize(16);
    }

}
