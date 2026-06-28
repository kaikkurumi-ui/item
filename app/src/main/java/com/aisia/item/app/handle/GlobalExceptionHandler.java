package com.aisia.item.app.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleRuntimeException(RuntimeException e){
        log.error("出错了:{}",e.getMessage());
        return "网络繁忙";
    }
}
