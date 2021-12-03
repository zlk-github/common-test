package consumer.listener.broadcasting;

import com.zlk.core.model.constant.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;

/**
 * 消费者--广播模式（1对多，相同消费组全量接收消息）
 *     注： 广播消费（Broadcasting），相同Consumer Group的每个Consumer实例都接收全量的消息。
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
//@Component
// 消费组rocketmq_group_1001，top为clustering-topic1
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_1,consumerGroup ="${rocketmq.consumer.group1}",messageModel = MessageModel.BROADCASTING)
public class ConsumerListener implements RocketMQListener<Message> {
    @Value("${rocketmq.consumer.group1}")
    private String groupName;

    @Override
    public void onMessage(Message message) {
        log.info("广播模式--》拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.TOPIC_1,message);
    }
}
