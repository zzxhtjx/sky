package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartServervice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 购物车
 * @Classname ShoppingCartController
 * @Date 2024/1/4 11:07
 * @Created by dongxuanmang
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartServervice shoppingCartServervice;

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    Result insert(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartServervice.add(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询购物车")
    Result<List<ShoppingCart>> getAll(){
        return Result.success(shoppingCartServervice.getAll(BaseContext.getCurrentId()));
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车某个商品")
    Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartServervice.delete(shoppingCartDTO);
        return Result.success();
    }

    @DeleteMapping("/clean")
    @ApiOperation("清除购物车")
    Result clean(){
        shoppingCartServervice.clean(BaseContext.getCurrentId());
        return Result.success();
    }

}
