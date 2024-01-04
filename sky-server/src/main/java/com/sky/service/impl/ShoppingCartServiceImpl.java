package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartServervice;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description TODO
 * @Classname ShoppingCartServiceImpl
 * @Date 2024/1/4 11:18
 * @Created by dongxuanmang
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartServervice {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车中是否已经存在该数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list != null && list.size() > 0){
            //update 更新数据
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else {
            //insert
            if(shoppingCartDTO.getDishId() != null){
                //更新菜品
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                ShoppingCart cart = ShoppingCart.builder()
                        .name(dish.getName())
                        .userId(BaseContext.getCurrentId())
                        .dishId(dish.getId())
                        .dishFlavor(shoppingCartDTO.getDishFlavor())
                        .number(1)
                        .amount(dish.getPrice())
                        .image(dish.getImage())
                        .createTime(LocalDateTime.now()).build();
                shoppingCartMapper.insert(cart);
            }else if(shoppingCartDTO.getSetmealId() != null){
                SetmealVO setmealVO = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                ShoppingCart cart = ShoppingCart.builder()
                        .name(setmealVO.getName())
                        .userId(BaseContext.getCurrentId())
                        .setmealId(setmealVO.getId())
                        .dishFlavor(shoppingCartDTO.getDishFlavor())
                        .number(1)
                        .amount(setmealVO.getPrice())
                        .image(setmealVO.getImage())
                        .createTime(LocalDateTime.now()).build();
                shoppingCartMapper.insert(cart);
            }
        }
    }

    public List<ShoppingCart> getAll(Long userId) {
        return shoppingCartMapper.getAll(userId);
    }

    public void delete(ShoppingCartDTO shoppingCartDTO) {
        //首先获得现在的商品,然后减少一个
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        ShoppingCart cart = list.get(0);
        if(cart.getNumber().equals(1)){
            //判断是否需要直接删除
            shoppingCartMapper.delete(shoppingCart);
        }else {
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.updateNumberById(cart);
        }
    }

    public void clean(Long userId) {
        shoppingCartMapper.clean(userId);
    }
}
