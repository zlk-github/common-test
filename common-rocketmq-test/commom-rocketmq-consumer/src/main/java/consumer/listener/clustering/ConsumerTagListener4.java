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

import java.nio.charset.StandardCharsets;

/**
 * 消费者(tag1+tag2过滤消息)--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 *     注：
 *     tag模式下需要集群消费需要使用不同的消费组（类似于同一个消费组的广播消费模式），否则存在交叉的tag消息也会被均分导致少消费数据，如不存在交叉tag则不会存在该问题。
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

/**
 * 消费者(tag1+tag2过滤消息)
 */
@Slf4j
// @Component
// 消费组rocketmq_group_1007，top为clustering-topic7; TAG_1_OR_TAG_2 = "tag1||tag2";
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_7,consumerGroup ="${rocketmq.consumer.group7_4}",
        selectorExpression = RocketMQConstant.TAG_1_OR_TAG_2)
public class ConsumerTagListener4 implements RocketMQListener<MessageExt> , RocketMQPushConsumerLifecycleListener {
    @Value("${rocketmq.consumer.group7_4}")
    private String groupName;
    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String((byte[]) messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("tag(tag2||tag2)过滤消息。拿到消费组：{}，consumerGroup：{}，主题Top:{}下消息,tag:{},消息：{}","rocketmq_consumer_group_1007_4",
                groupName,RocketMQConstant.TOPIC_7,messageExt.getTags(),message);
    }
    @SneakyThrows
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 过滤获取到RocketMQConstant.TOPIC_7-4下tag:(tag2||tag2)
        //defaultMQPushConsumer.subscribe(RocketMQConstant.TOPIC_7_4, RocketMQConstant.TAG_1_OR_TAG_2);
        // 设置最大重试次数
        defaultMQPushConsumer.setMaxReconsumeTimes(5);
        // 如下，设置其它consumer相关属性
        defaultMQPushConsumer.setPullBatchSize(16);
        /*
        defaultMQPushConsumer.setConsumeThreadMin(2); //消费端拉去到消息以后分配线程去消费
        defaultMQPushConsumer.setConsumeThreadMax(10);//最大消费线程，一般情况下，默认队列没有塞满，是不会启用新的线程的
        defaultMQPushConsumer.setPullInterval(1000);//消费端多久一次去rocketMq 拉去消息
        defaultMQPushConsumer.setPullBatchSize(32);     //消费端每个队列一次拉去多少个消息，若该消费端分配了N个监控队列，那么消费端每次去rocketMq拉去消息说为N*1
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        defaultMQPushConsumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(2); //单次消费时一次性消费多少条消息，批量消费接口才有用，可选配置
        */
    }

}
