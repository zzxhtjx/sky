package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
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

    void save(List<SetmealDish> setmealDishes);


    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delete(Long id);

    void deleteByIds(List<Long> ids);

    void update(List<SetmealDish> setmealDishes);
}
