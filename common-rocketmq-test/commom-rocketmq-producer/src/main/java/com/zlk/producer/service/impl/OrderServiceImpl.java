package com.zlk.producer.service.impl;

import com.zlk.core.model.dto.OrderDTO;
import com.zlk.producer.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2021/11/23/023 19:30
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public void add(OrderDTO orderDTO) {
        // 模拟入库
    }
}
