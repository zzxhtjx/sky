package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @Description TODO
 * @Classname UserServerice
 * @Date 2024/1/2 21:38
 * @Created by dongxuanmang
 */
public interface UserServerice {
    User wxLogin(UserLoginDTO userLoginDTO);
}
