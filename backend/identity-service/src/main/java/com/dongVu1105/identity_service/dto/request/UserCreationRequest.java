package com.dongVu1105.identity_service.dto.request;

import com.dongVu1105.identity_service.entity.Role;
import jakarta.persistence.*;
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
public class UserCreationRequest {

    String email;
    String password;
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    String identityNumber;
    LocalDate birthday;
    boolean gender;
    Set<String> role;
}
