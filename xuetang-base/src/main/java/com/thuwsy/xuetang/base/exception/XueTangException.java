package com.thuwsy.xuetang.base.exception;

/**
 * ClassName: XueTangException
 * Package: com.thuwsy.xuetang.base.exception
 * Description: 项目自定义异常类
 *
 * @Author THU_wsy
 * @Create 2024/1/17 14:09
 * @Version 1.0
 */
public class XueTangException extends RuntimeException {
    private String errMessage;

    public XueTangException() {}

    public XueTangException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new XueTangException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new XueTangException(errMessage);
    }
}
