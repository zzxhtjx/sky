package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Classname AutoFillAspect
 * @Date 2023/12/25 19:53
 * @Created by lenovo
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /*
    *切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){
    }

    /*
    * 前置通知,进行公共字段的赋值
    */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws Exception{
        log.info("开始进行公共字段的填充……");
        //获取被当前拦截的方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//反射获取的是原本的注解的类
        OperationType value = autoFill.value();

        // 约定第一个参数是需要设置的对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object o = args[0];
        Class aClass = o.getClass();
        if(value == OperationType.UPDATE){
            aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(o, BaseContext.getCurrentId());
            aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(o, LocalDateTime.now());
        }else if(value == OperationType.INSERT){
            aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(o, BaseContext.getCurrentId());
            aClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(o, LocalDateTime.now());
            aClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(o, BaseContext.getCurrentId());
            aClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(o, LocalDateTime.now());
        }
    }
}
