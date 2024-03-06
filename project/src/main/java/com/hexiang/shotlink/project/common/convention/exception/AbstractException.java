package com.hexiang.shotlink.project.common.convention.exception;


import com.hexiang.shotlink.project.common.convention.errorcode.IErrorCode;
import lombok.Getter;


/**
 * 全局异常拦截器抽象类
 */
@Getter
public class AbstractException extends RuntimeException{
    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode iErrorCode) {
        super(message, throwable);
        this.errorCode = iErrorCode.code();
        this.errorMessage = iErrorCode.message();
    }
}
