package com.hexiang.shotlink.admin.common.convention.exception;

import com.hexiang.shotlink.admin.common.convention.errorcode.BaseErrorCode;
import com.hexiang.shotlink.admin.common.convention.errorcode.IErrorCode;

public class RemoteException extends AbstractException{
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
}
