package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 菜品相关
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

    // TODO 查询的图片具有问题,因为数据库中的链接和自己阿里云配置有差异
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        if(pageResult != null){
            return Result.success(pageResult);
        }else {
            return Result.error(null);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishDTO> getById(@PathVariable Long id){
        //先获得dish对象
        Dish dish = dishService.getDishById(id);
        //获得flavors
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish, dishDTO);
        dishDTO.setFlavors(dishService.getFlavorsById(id));
        return Result.success(dishDTO);
    }

    //TODO 什么时候使用这个接口
    @GetMapping("/list")
    @ApiOperation("根据id查询菜品")
    public Result<List<Dish>> getByCategoryId(Long categoryId){
        List<Dish> data = dishService.getDishByCategoryId(categoryId);
        return Result.success(data);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids){
        dishService.delete(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改状态")
    public Result<String> status(@PathVariable int status, Long id){
        dishService.modifyStatus(status, id);
        return Result.success();
    }
}
