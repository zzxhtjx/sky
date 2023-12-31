package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @Description TODO
 * @Classname DishService
 * @Date 2023/12/30 13:54
 * @Created by dongxuanmang
 */
public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    Dish getDishById(Long id);

    List<DishFlavor> getFlavorsById(Long id);

    List<Dish> getDishByCategoryId(Long categoryId);

    String getCategoryName(Long categoryId);

    void update(DishDTO dishDTO);

    void delete(List<Long> data);

    void modifyStatus(int status, Long id);

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
