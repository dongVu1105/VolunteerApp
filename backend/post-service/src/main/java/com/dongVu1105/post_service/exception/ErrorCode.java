package com.dongVu1105.post_service.exception;

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
    EVENT_NOT_EXISTED (1010, "event not existed", HttpStatus.NOT_FOUND),
    UNCONNECTED_SERVICE (1011, "service was not connected", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_EVENT (1012, "you cannot delete this event", HttpStatus.BAD_REQUEST),
    USER_REGISTERED(1013, "Oops!: You registered.", HttpStatus.BAD_REQUEST),
    USER_NOT_REGISTERED(1014, "Oops!: You didn't register.", HttpStatus.BAD_REQUEST),
    EVENT_USER_NOT_EXISTED (1015, "this user has not attend this event", HttpStatus.NOT_FOUND),
    UNAVAILABLE_EVENT (1016, "event has not accepted by admin", HttpStatus.BAD_REQUEST),
    CANNOT_MODIFY_EVENT (1017, "Only the event manager can modify this event", HttpStatus.BAD_REQUEST),
    CANNOT_POST (1018, "Only the participants can post in event", HttpStatus.BAD_REQUEST),
    CANNOT_REACT (1019, "Only the participants can react in this post", HttpStatus.BAD_REQUEST),
    CANNOT_COMMENT (1020, "Only the participants can create comment and delete comment in this post", HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED (1021, "Post not found", HttpStatus.NOT_FOUND),
    REACT_NOT_EXISTED (1022, "react not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_EXISTED (1023, "Comment not found", HttpStatus.NOT_FOUND),
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
