package consumer.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 消费者配置类
 * @author likuan.zhou
 * @date 2021/10/29/029 9:12
 */
@Data
@Configuration
/**读取前缀为rocketmq.consumer的配置*/
@ConfigurationProperties(prefix = "rocketmq.consumer")
@Slf4j
public class RocketMQConsumerCofig {

    /**消息分组*/
    private String groupName;
    /**MP的ip与端口*/
    private String namesrvAddr;
    /**主题规则*/
    private String topics;
    /**消费者最小线程数据量*/
    private Integer consumeThreadMin;
    /**消费者最大线程数据量*/
    private Integer consumeThreadMax;
    /**设置一次消费信心的条数，默认1*/
    private Integer consumeMessageBatchMaxSize;

    /**
     * rocketmq 消费者配置类
     * @return DefaultMQProducer对象
     */
    @Bean
    /**prefix为配置文件前缀。name为配置的名字。value为配置的值。havingValue是与配置的值（value）对比值,当两个值相同返回true,配置类生效。*/
    // @ConditionalOnProperty(prefix = "rocketmq.consumer",value = "isOnOff",havingValue = "on")
    // 需要使用spring.rocketmq配置
    public DefaultMQPushConsumer defaultMqProducer() {
        log.info("defaultMQProducer开始创建-------------------------------");
        // 消息分组
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);
        //启动
        try {
            // rocketmq ip与端口
            defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
            defaultMQPushConsumer.setVipChannelEnabled(false);
            defaultMQPushConsumer.start();
            log.info("defaultMQProducer创建成功-------------------------------");
            return defaultMQPushConsumer;
        } catch (MQClientException e) {
            log.info("defaultMQProducer创建失败，配置:{}，失败原因",defaultMQPushConsumer,e);
            e.printStackTrace();
        }
        return null;
    }

}