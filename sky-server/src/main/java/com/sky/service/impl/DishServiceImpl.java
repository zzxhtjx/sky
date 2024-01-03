package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.CategoryTypeConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
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

    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<Dish> dataDish = dishMapper.pageQuery(dishPageQueryDTO);
        Page<DishVO> dataDishVO = new Page<DishVO>();
        for(Dish dish : dataDish){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            dishVO.setCategoryName(getCategoryName(dish.getCategoryId()));
            dataDishVO.add(dishVO);
        }
        return new PageResult(dataDishVO.size(), dataDishVO);
    }

    public Dish getDishById(Long id) {
        return dishMapper.getById(id);
    }

    public List<DishFlavor> getFlavorsById(Long id) {
        return dishFlavorMapper.getById(id);
    }

    public String getCategoryName(Long categoryId){
        return categoryMapper.getCategoryName(categoryId, CategoryTypeConstant.dish);
    }

    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    @Transactional
    public void update(DishDTO dishDTO){
        //菜品内容的修改, 以及口味的修改
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        //然后是口味表的修改
        Long dishId = dish.getId();
        dishFlavorMapper.delete(dishId);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insert(flavors);
        }
    }

    @Transactional
    public void delete(List<Long> ids) {
        //判断是否存在起售中的菜品数据
        for(Long id : ids){
            Dish dish = getDishById(id);
            if(dish.getStatus().equals(StatusConstant.ENABLE))   {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断是否存在套餐中的菜品数据
        List<Long> setmealIds = getSetmealIdsByDishIds(ids);
        if(setmealIds != null && !setmealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByIds(ids);
    }

    public void modifyStatus(int status, Long id) {
        dishMapper.modifyStatus(status, id);
    }


    public List<Long> getSetmealIdsByDishIds(List<Long> dishIds) {
        return setmealDishMapper.getSetmealIdsByDishIds(dishIds);
    }

    /**
     * 条件查询菜品和口味
     * @param categoryId
     * @return
     */
    public List<DishVO> listWithFlavor(Long categoryId) {
        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getById(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
