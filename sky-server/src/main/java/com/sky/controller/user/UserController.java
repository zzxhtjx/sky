package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserServerice;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 用户微信登陆
 * @Classname UserController
 * @Date 2024/1/2 21:32
 * @Created by dongxuanmang
 */
@RestController
@RequestMapping("/user/user")
@ApiOperation("用户相关")
@Slf4j
public class UserController {
    @Autowired
    private UserServerice userServerice;

    @Autowired
    private JwtProperties jwtProperties;
    /**
     * description: 微信登录
     * @since: 1.0.0
     * @author: dongxuanmang
     * @date: 2024/1/2 22:19
     * @param userLoginDTO
     * @return com.sky.result.Result<com.sky.vo.UserLoginVO>
     */
    @PostMapping("/login")
    @ApiOperation("用户微信登录")
    Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录: {}", userLoginDTO.getCode());

        //微信登录
        User user = userServerice.wxLogin(userLoginDTO);

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .openid(user.getOpenid()).build();
        return Result.success(userLoginVO);
    }
}
