package com.thuwsy.xuetang.auth.exception;

import com.thuwsy.xuetang.base.exception.CommonError;
import com.thuwsy.xuetang.base.exception.RestErrorResponse;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.base.model.AuthErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.thuwsy.xuetang.auth.exception
 * Description: 全局异常处理器
 *
 * @Author THU_wsy
 * @Create 2024/1/17 14:31
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AuthErrorResponse handleException(Exception e) {
        log.error("【认证授权异常】{}", e.getMessage());
        return new AuthErrorResponse("invalid_grant", "用户名或密码错误");
    }
}
