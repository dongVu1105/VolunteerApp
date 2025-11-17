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
    USER_EXISTED (1005, "user existed", HttpStatus.BAD_REQUEST),
    UNABLE_ACCOUNT (1006, "account was unable!", HttpStatus.BAD_REQUEST),
    CAN_NOT_CREATE_TOKEN (1007, "cannot create token", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED (1008, "role not existed", HttpStatus.NOT_FOUND),
    FAIL_REGISTRATION (1009, "register fail!", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(1010, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1011, "Invalid email address", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(1012, "Email is required", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1013, "invalid key error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1014, "your username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1015, "existed username", HttpStatus.BAD_REQUEST)
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
