package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @Description TODO
 * @Classname ShoppingCartServervice
 * @Date 2024/1/4 11:18
 * @Created by dongxuanmang
 */
public interface ShoppingCartService {
    void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> getAll(Long userId);

    void delete(ShoppingCartDTO shoppingCartDTO);

    void clean(Long userId);
}
