package com.thuwsy.xuetang.checkcode.controller;

import com.thuwsy.xuetang.checkcode.dto.CheckCodeParamsDto;
import com.thuwsy.xuetang.checkcode.dto.CheckCodeResultDto;
import com.thuwsy.xuetang.checkcode.service.CheckCodeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: CheckCodeController
 * Package: com.thuwsy.xuetang.checkcode.controller
 * Description: 验证码服务接口
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:45
 * @Version 1.0
 */
@RestController
@RequestMapping("/checkcode")
public class CheckCodeController {

    @Resource(name = "PicCheckCodeService")
    private CheckCodeService picCheckCodeService;


    /**
     * 生成验证信息
     */
    @PostMapping(value = "/pic")
    public CheckCodeResultDto generatePicCheckCode(CheckCodeParamsDto checkCodeParamsDto){
        return picCheckCodeService.generate(checkCodeParamsDto);
    }

    /**
     * 校验
     */
    @PostMapping(value = "/verify")
    public Boolean verify(String key, String code){
        Boolean isSuccess = picCheckCodeService.verify(key,code);
        return isSuccess;
    }
}
