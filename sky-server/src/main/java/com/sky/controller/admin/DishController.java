package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Classname DishController
 * @Date 2023/12/30 13:20
 * @Created by dongxuanmang
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    /**
     * description: 
     * @since: 1.0.0
     * @author: dongxuanmang
     * @date: 2023/12/30 15:47
     * @param dishDTO
     * @return com.sky.result.Result<java.lang.String>
     */
    @PostMapping
    @ApiOperation("新增菜品")
    Result<String> save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品 {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

}
