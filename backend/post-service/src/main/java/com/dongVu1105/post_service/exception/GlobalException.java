package com.dongVu1105.post_service.exception;



import com.dongVu1105.post_service.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalException {
//    @ExceptionHandler(value = RuntimeException.class)
//    public ResponseEntity<ApiResponse> handleRuntimeException (RuntimeException e){
//        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
//    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException (AppException appException){
        ErrorCode errorCode = appException.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException (AccessDeniedException e){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
    }
}
