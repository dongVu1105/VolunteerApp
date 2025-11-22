package com.dongVu1105.AI_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1000, "this exception is not categorized", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "invalid key error", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "user not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED (1003, "unauthenticated exception", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED (1004, "unauthorized exception", HttpStatus.FORBIDDEN),
    USER_EXISTED(1005, "user existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1006, "role not existed", HttpStatus.NOT_FOUND),
    CAN_NOT_CREATE_TOKEN(1007, "can not create token", HttpStatus.BAD_REQUEST),
    TOKEN_VALIDATED(1008, "token validated", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1009, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1010, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1011, "Invalid email address", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(1012, "Email is required", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD(1013, "incorrect password", HttpStatus.BAD_REQUEST),
    INVALID_BIRTH(1014, "your age must be at least {min} years old", HttpStatus.BAD_REQUEST),
    FILE_NOT_EXISTED(1015, "file not existed", HttpStatus.NOT_FOUND),
    GET_PROFILE_FAIL (1016, "get profile fail", HttpStatus.BAD_REQUEST),
    CONVERSATION_NOT_FOUND (1017, "conversation not found", HttpStatus.NOT_FOUND)

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
