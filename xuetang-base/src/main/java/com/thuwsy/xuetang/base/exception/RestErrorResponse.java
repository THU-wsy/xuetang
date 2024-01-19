package com.thuwsy.xuetang.base.exception;

import java.io.Serializable;

/**
 * ClassName: RestErrorResponse
 * Package: com.thuwsy.xuetang.base.exception
 * Description: 封装错误响应
 *
 * @Author THU_wsy
 * @Create 2024/1/17 14:13
 * @Version 1.0
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
