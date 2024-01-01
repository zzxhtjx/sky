package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/status/{status}")
    @ApiOperation("修改状态")
    Result<String> modifyStatus(@PathVariable int status, Long id){
        setmealService.modifyStatus(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取数据")
    Result<SetmealVO> getById(@PathVariable Long id){
        return Result.success(setmealService.getById(id));
    }

    @PutMapping
    @ApiOperation("修改套餐")
    Result<String> update(@RequestBody SetmealDTO setmealDTO){
        if(setmealService.update(setmealDTO)){
            return Result.success();
        }else {
            return Result.error("修改失败");
        }
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    Result<String> delete(@RequestParam List<Long> ids){
        if(setmealService.delete(ids)){
            return Result.success();
        }else {
            return Result.error("存在起售中的套餐不能删除");
        }
    }
}
