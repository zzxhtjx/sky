package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

/**
 * @Description TODO
 * @Classname SetmealService
 * @Date 2023/12/31 20:37
 * @Created by dongxuanmang
 */
public interface SetmealService {
    void save(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void modifyStatus(int status, Long id);

    SetmealVO getById(Long id);
}
