package org.telemedicine.server.dto.patients;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientUpdateRequest {
    String fullName;
    String password;
}
