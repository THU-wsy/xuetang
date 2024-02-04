package com.thuwsy.xuetang.auth.service;

import com.thuwsy.xuetang.auth.dto.FindPasswordDto;
import com.thuwsy.xuetang.auth.dto.RegisterDto;

/**
 * ClassName: VerifyService
 * Package: com.thuwsy.xuetang.auth.service
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:50
 * @Version 1.0
 */
public interface VerifyService {
    void findPassword(FindPasswordDto dto);
    void register(RegisterDto registerDto);
}
