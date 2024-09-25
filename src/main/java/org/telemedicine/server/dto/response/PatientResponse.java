package org.telemedicine.server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientResponse {
    String id;
    String fullName;
    String email;
    Set<String> roles;
}
