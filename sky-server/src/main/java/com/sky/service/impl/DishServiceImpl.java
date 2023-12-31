package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Classname DishServiceImpl
 * @Date 2023/12/30 13:54
 * @Created by dongxuanmang
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * description:多张表操作需要添加事务注解
     * @since: 1.0.0
     * @author: dongxuanmang
     * @date: 2023/12/30 15:53
     * @param dishDTO
     * @return void
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //插入菜品表数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        //获取insert生成的主键值
        Long id = dish.getId();
        //插入口味表数据
        List<DishFlavor> dishFlavors =  dishDTO.getFlavors();
        if(dishFlavors != null && !dishFlavors.isEmpty()){
            dishFlavors.forEach( dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insert(dishFlavors);
        }
    }
}
