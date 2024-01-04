package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @Classname OrdersMapper
 * @Date 2024/1/4 17:33
 * @Created by dongxuanmang
 */
@Mapper
public interface OrdersMapper {

    void insert(Orders orders);
}
