package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Description 通用接口
 * @Classname CommonControl
 * @Date 2023/12/27 13:31
 * @Created by dongxuanmang
 */
@Api(tags = "上传文件")
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonControl {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        try{
            String originalFilename = file.getOriginalFilename();
            //截取扩展文件名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //拼接UUID获取最后的文件名
            String objectName = UUID.randomUUID() + extension;
            String url = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(url);
        }catch (Exception e){
            log.info("文件上传失败 {}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
