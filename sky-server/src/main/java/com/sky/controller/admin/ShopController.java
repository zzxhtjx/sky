package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 店铺相关
 * @Classname ShopController
 * @Date 2024/1/1 19:44
 * @Created by dongxuanmang
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY = "status";
    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result<String> setStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取状态")
    Result<Integer> get(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        if(status != null){
            return Result.success(status);
        }else {
            return Result.error("获取状态失败");
        }
    }
}
