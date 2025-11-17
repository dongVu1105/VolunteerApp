package com.dongVu1105.event_service.exception;

import jakarta.validation.constraints.NotNull;
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
    CANNOT_UNSUBSCRIBE_EVENT(1018, "Event is ongoing, you cannot unsubscribe", HttpStatus.BAD_REQUEST),
    FINISHED_EVENT(1018, "Event finished", HttpStatus.BAD_REQUEST),
    INVALID_STARTDATE(1019, "start date must be after now", HttpStatus.BAD_REQUEST),
    INVALID_FINISHDATE(1020, "finish date must be after start date", HttpStatus.BAD_REQUEST),
    INVALID_TITLE(1021, "event title must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1022, "invalid key error", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED(1023, "field required", HttpStatus.BAD_REQUEST)
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
