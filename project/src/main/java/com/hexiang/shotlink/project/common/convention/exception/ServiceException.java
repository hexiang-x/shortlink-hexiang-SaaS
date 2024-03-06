package com.hexiang.shotlink.project.common.convention.exception;


import com.hexiang.shotlink.project.common.convention.errorcode.BaseErrorCode;
import com.hexiang.shotlink.project.common.convention.errorcode.IErrorCode;

public class ServiceException extends AbstractException{

    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServiceException(IErrorCode errorCode) {
        this(null, errorCode);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }


    public ServiceException(String message, Throwable throwable, IErrorCode iErrorCode) {
        super(message, throwable, iErrorCode);
    }
}
