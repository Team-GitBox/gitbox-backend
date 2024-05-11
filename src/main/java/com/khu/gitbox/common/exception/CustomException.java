package com.khu.gitbox.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus errorCode;

    public CustomException(HttpStatus errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
