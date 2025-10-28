package com.dongVu1105.identity_service.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_EXISTED (1001, "user not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED (1002, "unauthenticated - you are not allowed!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED (1003, "unauthorized - you are not allowed!", HttpStatus.FORBIDDEN),
    UNCATEGORIZED (1004, "uncategorized exception", HttpStatus.BAD_REQUEST),
    USER_EXISTED (1005, "user existed", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
