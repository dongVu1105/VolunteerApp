package com.dongVu1105.identity_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T> {
    @Builder.Default
    int code = 0;
    @Builder.Default
    String message = "success!";
    T data;

}
