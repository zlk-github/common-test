package com.zlk.producer.controller.clustering;

import com.zlk.core.model.constant.RocketMQConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者--集群模式（1对1）
 *     注：集群消费（Clustering）：相同消费组下的消费者都会平均分摊消息。（1对1）
 * @author likuan.zhou
 * @date 2021/11/1/001 8:33
 */
@RestController
@RequestMapping("producer")
@Slf4j
@Api(tags = "生产者--集群模式（1对1）")
public class ProducerController {
    @Autowired
    private DefaultMQProducer mqProducer;

    @PostMapping("/send")
    @ApiOperation("发送普通消息")
    public void send(String msg){
        if (StringUtils.isBlank(msg)) {
            log.info("消息为空");
            return;
        }
        try {
            Message message = new Message(RocketMQConstant.CLUSTERING_TOPIC, RocketMQConstant.CLUSTERING_TAG, msg.getBytes());
            // 默认发送超时时间为3秒
            SendResult send = mqProducer.send(message);
            // 仅仅只是为了看输出，线上不要打该日志，否认会导致效率问题与占用大量服务器存储空间。
            log.info("消息发送成功");
        }catch (Exception ex) {
            log.error("消息发送失败，MQ主机信息：{}，Top:{},Tag:{},消息:{}",mqProducer.getNamesrvAddr(),RocketMQConstant.CLUSTERING_TOPIC,RocketMQConstant.CLUSTERING_TAG,msg,ex);
        }
    }

}
