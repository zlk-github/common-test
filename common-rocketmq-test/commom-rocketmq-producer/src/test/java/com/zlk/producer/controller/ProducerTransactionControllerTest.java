package com.zlk.producer.controller;

import com.zlk.core.model.dto.OrderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 生产者测试用例(事务消息)
 * @author likuan.zhou
 * @date 2021/11/29/001 8:33
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTransactionControllerTest {

    @Autowired
    private ProducerTransactionController producerTransactionController;


    @Test
    public void testTransactionSend(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(10001L);
        orderDTO.setStatus(1);
        orderDTO.setName("这是一个订单，订单号为10001（事务消息）");
        producerTransactionController.transactionSend(orderDTO);
    }

    @Test
    public void testTransactionSend2(){
        producerTransactionController.transactionSend2("这是一条事务消息！！！！");
    }
}
