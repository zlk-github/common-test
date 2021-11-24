package com.zlk.producer.config;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2021/11/24/024 9:09
 */
// 这个RocketMQTemplate的Spring Bean名是'extRocketMQTemplate', 与所定义的类名相同(但首字母小写)
/*@ExtRocketMQTemplateConfiguration(nameServer="127.0.0.1:9876"
        , ... // 定义其他属性，如果有必要。
)*/
@ExtRocketMQTemplateConfiguration
public class ExtRocketMQTemplate extends RocketMQTemplate {
}
