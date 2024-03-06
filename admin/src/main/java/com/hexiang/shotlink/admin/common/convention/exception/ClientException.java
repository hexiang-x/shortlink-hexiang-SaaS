package com.hexiang.shotlink.admin.common.convention.exception;


import com.hexiang.shotlink.admin.common.convention.errorcode.BaseErrorCode;
import com.hexiang.shotlink.admin.common.convention.errorcode.IErrorCode;

/**
 * 客户端全局异常类
 */
public class ClientException extends AbstractException{
    public ClientException(IErrorCode iErrorCode) {
        this(null, null, iErrorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode iErrorCode) {
        this(message, null, iErrorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode iErrorCode) {
        super(message, throwable, iErrorCode);
    }
}
