package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 套餐相关
 * @Classname SetmealController
 * @Date 2023/12/31 20:32
 * @Created by dongxuanmang
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    Result<String> save(@RequestBody  SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        return Result.success(setmealService.pageQuery(setmealPageQueryDTO));
    }
}
