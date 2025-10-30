package com.dongVu1105.profile_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userId;
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    LocalDate birthday;
    boolean gender;
}
