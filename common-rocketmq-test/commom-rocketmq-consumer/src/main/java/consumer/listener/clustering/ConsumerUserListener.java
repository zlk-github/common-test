package consumer.listener.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import com.zlk.core.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;

/**
 * 消费者(自定义消息体)--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */

@Slf4j
//@Component
// 消费组rocketmq_group_1002，top为clustering-topic2
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_2,consumerGroup ="${rocketmq.consumer.group2}")
public class ConsumerUserListener implements RocketMQListener<UserVO> {
    @Value("${rocketmq.consumer.group2}")
    private String groupName;

    @Override
    public void onMessage(UserVO message) {
        log.info("自定义消息体。拿到消费组：{}，主题Top:{}下消息。消息：{}",groupName,RocketMQConstant.TOPIC_2,message);
    }


}
