package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Classname SetmealServiceImpl
 * @Date 2023/12/31 20:37
 * @Created by dongxuanmang
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach( setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.save(setmealDishes);
    }

    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> data = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(data.size(), data);
    }


    public void modifyStatus(int status, Long id) {
       setmealMapper.modifyStatus(status, id);
    }

    public SetmealVO getById(Long id){
        return setmealMapper.getById(id);
    }

    @Transactional
    public boolean update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        int status = setmealMapper.getStatusById(setmealDTO.getId());
        if(status == StatusConstant.ENABLE){
            return false;
        }
        setmealMapper.update(setmeal);
        setmealDishMapper.delete(setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach( setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.update(setmealDishes);
        return true;
    }

    @Transactional
    public boolean delete(List<Long> ids){
        //判断套餐的状态
        List<Integer> status = setmealMapper.getStatusByIds(ids);
        for(int statu : status){
            if(statu == StatusConstant.ENABLE){
                return false;
            }
        }
        //删除套餐
        setmealMapper.deleteByIds(ids);
        //删除套餐和菜单的对应关系
        setmealDishMapper.deleteByIds(ids);
        return true;
    }

}
