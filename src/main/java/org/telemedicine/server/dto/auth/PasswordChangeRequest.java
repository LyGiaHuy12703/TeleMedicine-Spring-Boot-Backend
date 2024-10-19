package org.telemedicine.server.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeRequest {
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;
    @Size(min = 6, message = "PASSWORD_INVALID")
    private String newPassword;
}
