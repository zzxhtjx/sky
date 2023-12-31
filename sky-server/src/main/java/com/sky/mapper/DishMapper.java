package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<Dish> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);

    @Update("update dish set " +
            "name = #{name}, category_id = #{categoryId}, " +
            "price = #{price}, image = #{image}, " +
            "description = #{description}, status = #{status}, " +
            "create_time = #{createTime}, update_time = #{updateTime}, " +
            "create_user = #{createUser}, update_user = #{updateUser} " +
            "where id = #{id}")
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    @Delete("delete from dish where id = #{dishId}")
    void delete(Long dishId);

    @Update("update dish set status = #{status} where id = #{id}")
    void modifyStatus(int status, Long id);

    void deleteByIds(List<Long> dishIds);
}
