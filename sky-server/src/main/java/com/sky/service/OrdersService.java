package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @Description TODO
 * @Classname OrdersServervice
 * @Date 2024/1/4 17:32
 * @Created by dongxuanmang
 */
public interface OrdersService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
