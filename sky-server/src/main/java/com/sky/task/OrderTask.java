package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 自定义定时任务类
 * @Classname MyTask
 * @Date 2024/1/6 14:01
 * @Created by dongxuanmang
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper ordersMapper;

    //处理订单超时的情况
    @Scheduled(cron = "0 * * * * ?")//每分钟触发一次
    public void processTimeoutOrder(){
        log.info("定时处理超时订单: {}", LocalDateTime.now());
        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if(ordersList != null && !ordersList.isEmpty()){
            for(Orders orders : ordersList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时,自动取消");
                orders.setCancelTime(LocalDateTime.now());
                ordersMapper.update(orders);
            }
        }
    }

    //处理一直处于派送中的订单
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单: {}", LocalDateTime.now());
        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));

        if(ordersList != null && !ordersList.isEmpty()){
            for(Orders orders : ordersList){
                orders.setStatus(Orders.COMPLETED);
                ordersMapper.update(orders);
            }
        }
    }
}
