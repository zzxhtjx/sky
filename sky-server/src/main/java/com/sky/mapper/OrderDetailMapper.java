package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Classname OrderDetail
 * @Date 2024/1/4 18:19
 * @Created by dongxuanmang
 */
@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);
}
