package com.thuwsy.xuetang.auth.service;

import com.thuwsy.xuetang.auth.dto.AuthParamsDto;
import com.thuwsy.xuetang.auth.dto.RegisterDto;

/**
 * ClassName: XcUserService
 * Package: com.thuwsy.xuetang.auth.service
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/30 22:20
 * @Version 1.0
 */
public interface XcUserService {
    String login(AuthParamsDto authParamsDto);
}
