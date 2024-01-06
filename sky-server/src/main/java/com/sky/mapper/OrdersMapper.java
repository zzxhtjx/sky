package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Classname OrdersMapper
 * @Date 2024/1/4 17:33
 * @Created by dongxuanmang
 */
@Mapper
public interface OrdersMapper {

    void insert(Orders orders);


    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Update("update orders set status = #{status} where id = #{id}")
    void cancelById(Long id, Integer status);

    @Select("select count(*) from orders where status = #{status}")
    Integer getStatusCount(int status);

    @Update("update orders set status = #{status} where id = #{id}")
    void setStatus(Long id, Integer status);

    @Update("update orders set status = #{status} where id = #{id}")
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    @Update("update orders set status = #{status} ,rejection_reason = #{ordersRejectionDTO.rejectionReason} where id = #{ordersRejectionDTO.id}")
    void reject(OrdersRejectionDTO ordersRejectionDTO, Integer status);

    @Update("update orders set status = #{status} ,cancel_reason = #{cancelReason} where id = #{id}")
    void cancel(OrdersCancelDTO ordersCancelDTO, Integer status);

    /**
     * description:根据订单状态和超时时间查询订单,并取消
     * @since: 1.0.0
     * @author: dongxuanmang
     * @date: 2024/1/6 14:21
     * @param status
     * @param orderTime
     * @return java.util.List<com.sky.entity.Orders>
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    Double getByMap(Map map);
}
