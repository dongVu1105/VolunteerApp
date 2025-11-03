package com.dongVu1105.identity_service.dto.response;

import com.dongVu1105.identity_service.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;


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
    String identityNumber;
    boolean gender;
    Set<Role> role;
}
