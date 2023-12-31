package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getById(Long id);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void delete(Long dishId);

}
