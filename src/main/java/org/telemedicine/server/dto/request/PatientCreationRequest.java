package org.telemedicine.server.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientCreationRequest {
    String id;
    @Size(min = 5, message = "FULL_NAME_INVALID")
    String fullName;
    @Email(message = "INVALID_EMAIL")
    String email;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String verifyCode;
    boolean verified=false;
}
