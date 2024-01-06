package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @Description TODO
 * @Classname OrdersServervice
 * @Date 2024/1/4 17:32
 * @Created by dongxuanmang
 */
public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    OrderVO getOrderById(Long id);

    PageResult pageQuery(OrdersPageQueryDTO  pageQueryDTO);

    void cancelById(Long id);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void repetition(Long id);

    void reminder(Long id);

    OrderStatisticsVO getStatistics();

    void setStatus(Long id, Integer status);

    void reject(OrdersRejectionDTO ordersRejectionDTO);
}
