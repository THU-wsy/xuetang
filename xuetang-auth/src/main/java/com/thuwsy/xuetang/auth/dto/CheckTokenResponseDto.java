package com.thuwsy.xuetang.auth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: CheckTokenResponseDto
 * Package: com.thuwsy.xuetang.auth.dto
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/31 11:49
 * @Version 1.0
 */
@Data
public class CheckTokenResponseDto {
    private String user_name;
    private Boolean active = true;
    private Long exp;
    private List<String> authorities = new ArrayList<>();
    private String jti;
}
