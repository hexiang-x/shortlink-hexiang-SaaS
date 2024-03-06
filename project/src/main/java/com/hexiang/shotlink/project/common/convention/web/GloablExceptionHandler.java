package com.hexiang.shotlink.project.common.convention.web;


import com.hexiang.shotlink.project.common.convention.exception.AbstractException;
import com.hexiang.shotlink.project.common.convention.result.Result;
import com.hexiang.shotlink.project.common.convention.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Component("globalExceptionHandlerByAdmin")
@Slf4j
@RestControllerAdvice
public class GloablExceptionHandler {
    /**
     * 拦截参数验证异常
     */


    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = {AbstractException.class})
    public Result abstactException(HttpServletRequest request, AbstractException abstractException){
        if(abstractException.getCause() != null){
            return Results.failure(abstractException);
        }
        return Results.failure(abstractException);
    }
}
