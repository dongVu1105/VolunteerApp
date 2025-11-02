package com.dongVu1105.event_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    String password;
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    LocalDate birthday;
    boolean gender;
}
