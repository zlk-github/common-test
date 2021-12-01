package com.zlk.producer.controller;

import com.zlk.core.model.constant.RocketMQConstant;
import com.zlk.core.model.dto.OrderDTO;
import com.zlk.producer.config.ExtRocketMQTemplate;
import com.zlk.producer.config.ExtRocketMQTemplate2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者--生产消息(事务消息)，发送到MQ
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
@RestController
@RequestMapping("producerTransaction")
@Slf4j
@Api(tags = "生产者")
public class ProducerTransactionController {
    // swagger : http://localhost:8031/swagger-ui.html
    // 同一台机器的同一条消息，MsgId是不一定一样，不要用来做业务。需要的话自己设置key。
    // 以下的log.info不要在生产环境使用，否则会生成大量日志

    @Autowired
    private ExtRocketMQTemplate extRocketMQTemplate;

    @Autowired
    private ExtRocketMQTemplate2 extRocketMQTemplate2;


    @PostMapping("/transaction/send")
    @ApiOperation("事务消息1")
    public void transactionSend(@RequestBody OrderDTO orderDTO){
        try {
            //  事务消息：RocketMQ采用了2PC的思想来实现了提交事务消息，同时增加一个补偿逻辑来处理二阶段超时或者失败的消息。（不支持延时消息和批量消息）
            for (int i = 0; i < 5; i++) {
                //destination为消息发送的topic，message为消息体，arg为传递给本地函数参数
                Message<OrderDTO> build = MessageBuilder.withPayload(orderDTO).setHeader("key", orderDTO.getId()).build();
                TransactionSendResult transaction = extRocketMQTemplate.sendMessageInTransaction(RocketMQConstant.CLUSTERING_TOPIC_9,build, i);
                log.info("发送状态：{}",transaction.getLocalTransactionState());
            }
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{}",extRocketMQTemplate.getProducer().getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_9,ex);
        }
    }

    @PostMapping("/transaction2/send")
    @ApiOperation("事务消息2")
    public void transactionSend2(@RequestBody String msg){
        try {
            //  事务消息：RocketMQ采用了2PC的思想来实现了提交事务消息，同时增加一个补偿逻辑来处理二阶段超时或者失败的消息。（不支持延时消息和批量消息）
            for (int i = 0; i < 5; i++) {
                //destination为消息发送的topic，message为消息体，arg为传递给本地函数参数
                TransactionSendResult transaction = extRocketMQTemplate2.sendMessageInTransaction(RocketMQConstant.CLUSTERING_TOPIC_9, MessageBuilder.withPayload(msg).build(), i);
                log.info("发送状态：{}",transaction.getLocalTransactionState());
            }
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{}",extRocketMQTemplate2.getProducer().getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC_9,ex);
        }
    }


}
