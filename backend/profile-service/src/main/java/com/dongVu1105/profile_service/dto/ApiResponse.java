package com.dongVu1105.profile_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    @Builder.Default
    int code = 0;
    @Builder.Default
    String message = "success!";
    T data;

}
