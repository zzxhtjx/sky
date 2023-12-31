package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Classname DishFlavorMapper
 * @Date 2023/12/30 14:59
 * @Created by dongxuanmang
 */
@Mapper
public interface DishFlavorMapper {

    void insert(List<DishFlavor> flavors);
}
