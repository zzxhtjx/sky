package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderSubmitVO;
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
 * @Classname OrderController
 * @Date 2024/1/4 17:23
 * @Created by dongxuanmang
 */
@RestController("UserController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户订单")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO orderSubmitVO =  ordersService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}
