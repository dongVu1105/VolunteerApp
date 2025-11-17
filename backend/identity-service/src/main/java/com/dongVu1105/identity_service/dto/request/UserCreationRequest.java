package com.dongVu1105.identity_service.dto.request;

import com.dongVu1105.identity_service.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    String email;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    @Size(min = 5, message = "INVALID_USERNAME")
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    String identityNumber;
    LocalDate birthday;
    String avatar;
    boolean gender;
    Set<String> role;
}
