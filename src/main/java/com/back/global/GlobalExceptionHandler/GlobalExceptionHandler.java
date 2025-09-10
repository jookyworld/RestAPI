package com.back.global.GlobalExceptionHandler;

import com.back.global.rsData.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RsData<Void> handle() {
        return new RsData<>(
                "404-1",
                "해당 데이터가 존재하지 않습니다."
        );
    }
}
