package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.constant.RequestParamConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserServerice;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Classname UserServiceImpl
 * @Date 2024/1/2 21:38
 * @Created by dongxuanmang
 */
@Service
@Slf4j
public class UserServiceImpl implements UserServerice {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    static final private String url = "https://api.weixin.qq.com/sns/jscode2session";

    static final private String grant_type = "authorization_code";

    public User wxLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口,获得当前用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        //如果openid为空,登录失败,抛出业务异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断是不是新用户,不是的话自动注册新的用户
        User user = userMapper.getByOpenid(openid);
        if(user == null){
            //创建新用户
            user = User.builder()
                .openid(openid)
                .createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }
        return user;
    }

    private String getOpenid(String code){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(RequestParamConstant.appid, weChatProperties.getAppid());
        paramMap.put(RequestParamConstant.secret, weChatProperties.getSecret());
        paramMap.put(RequestParamConstant.js_code, code);
        paramMap.put(RequestParamConstant.grant_type, grant_type);
        String json = HttpClientUtil.doGet(url, paramMap);

        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
