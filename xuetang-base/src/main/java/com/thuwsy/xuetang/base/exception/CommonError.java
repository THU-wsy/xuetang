package com.thuwsy.xuetang.base.exception;

/**
 * ClassName: CommonError
 * Package: com.thuwsy.xuetang.base.exception
 * Description: 通用错误信息
 *
 * @Author THU_wsy
 * @Create 2024/1/17 14:05
 * @Version 1.0
 */
public enum CommonError {
    UNKNOWN_ERROR("执行过程异常，请重试"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    CommonError(String errMessage) {
        this.errMessage = errMessage;
    }
}
