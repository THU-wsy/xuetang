package com.thuwsy.xuetang.content.exception;

import com.thuwsy.xuetang.base.exception.CommonError;
import com.thuwsy.xuetang.base.exception.RestErrorResponse;
import com.thuwsy.xuetang.base.exception.XueTangException;
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
 * Package: com.thuwsy.xuetang.content.exception
 * Description: 全局异常处理器
 *
 * @Author THU_wsy
 * @Create 2024/1/17 14:31
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理项目的自定义异常
     * @param e XueTangException异常
     */
    @ExceptionHandler(XueTangException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(XueTangException e) {
        log.error("【项目异常】{}", e.getErrMessage(), e);
        return new RestErrorResponse(e.getErrMessage());
    }

    /**
     * 处理参数校验失败的异常
     * @param e MethodArgumentNotValidException异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 获取错误信息
        BindingResult bindingResult = e.getBindingResult();
        List<String> list = bindingResult.getFieldErrors().stream()
                .map(item -> item.getDefaultMessage())
                .collect(Collectors.toList());
        // 拼接多个错误信息
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (String s : list) {
            stringJoiner.add(s);
        }
        String msg = stringJoiner.toString();

        // 返回错误信息
        log.error("【校验异常】{}", msg, e);
        return new RestErrorResponse(msg);
    }

    /**
     * 处理其他系统异常
     * @param e Exception异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        log.error("【系统异常】{}", e.getMessage(), e);
        return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
    }
}
