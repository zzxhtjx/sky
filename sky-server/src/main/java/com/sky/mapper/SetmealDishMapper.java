package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Classname setmealDishMapper
 * @Date 2023/12/31 19:39
 * @Created by dongxuanmang
 */
@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
