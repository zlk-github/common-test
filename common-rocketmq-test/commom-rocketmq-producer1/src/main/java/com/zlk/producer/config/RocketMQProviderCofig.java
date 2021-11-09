/*
package com.zlk.producer.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 * RocketMQ 生产者配置类
 * @author likuan.zhou
 * @date 2021/10/29/029 9:12
 *//*

@Data
@Configuration
*/
/**读取前缀为rocketmq.producer的配置*//*

@ConfigurationProperties(prefix = "rocketmq.producer")
@Slf4j
public class RocketMQProviderCofig {

*/
/*
    ################rocketmq配置################
    rocketmq :
        producer:
                # 是否开启自动配置
        isOnOff: on
            # 发送同一类消息设置为同一个group，保证唯一默认不需要设置，rocketmq会使用ip@pid（pid代表jvm名字）作为唯一标识
        groupName: "rocketmq_group_1001"
                # mq的nameserver地址
        namesrvAddr: 47.119.180.152:9876
        maxMessageSize: 4096
                # 发送消息超时时间，默认 3000
        sendMsgTimeOut: 3000
                # 发送消息失败重试次数，默认2
        retryTimesWhenSendFailed: 2
*//*


    */
/**消息分组*//*

    private String groupName;
    */
/**MP的ip与端口*//*

    private String namesrvAddr;
    */
/**消息最大值*//*

    private Integer maxMessageSize;
    */
/**消息发送超时时间*//*

    private Integer sendMsgTimeOut;
    */
/**失败重试次数*//*

    private Integer retryTimesWhenSendFailed;


    */
/**
     * rocketmq 生产者配置类
     * @return DefaultMQProducer对象
     *//*

    @Bean
    */
/**prefix为配置文件前缀。name为配置的名字。value为配置的值。havingValue是与配置的值（value）对比值,当两个值相同返回true,配置类生效。*//*

    // @ConditionalOnProperty(prefix = "rocketmq.producer",value = "isOnOff",havingValue = "on")
    // 需要使用spring.rocketmq配置
    public DefaultMQProducer defaultMqProducer() {
        log.info("defaultMQProducer开始创建-------------------------------");
        DefaultMQProducer defaultMqProducer = new DefaultMQProducer(groupName);
        //启动
        try {
            // 消息分组
            // rocketmq ip与端口
            defaultMqProducer.setNamesrvAddr(namesrvAddr);
            defaultMqProducer.setVipChannelEnabled(false);
            //消息最大长度
            defaultMqProducer.setMaxMessageSize(maxMessageSize);
            //消息发送超时时间
            defaultMqProducer.setSendMsgTimeout(sendMsgTimeOut);
            //失败重试次数
            defaultMqProducer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendFailed);
            defaultMqProducer.start();
            log.info("defaultMQProducer创建成功-------------------------------");
            return defaultMqProducer;
        } catch (MQClientException e) {
            log.info("defaultMQProducer创建失败，配置:{}，失败原因",defaultMqProducer,e);
            e.printStackTrace();
        }
        return null;
    }

}*/
