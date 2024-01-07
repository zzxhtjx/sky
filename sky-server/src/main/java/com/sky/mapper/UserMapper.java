package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description TODO
 * @Classname UserMapper
 * @Date 2024/1/2 21:39
 * @Created by dongxuanmang
 */
@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    @Select("select sum(1) from user where create_time < #{dateTime}")
    Long getUserBefore(LocalDateTime dateTime);

    @Select("select sum(1) from user where create_time > #{begin} and create_time < #{end}")
    Integer countByMap(Map map);
}
