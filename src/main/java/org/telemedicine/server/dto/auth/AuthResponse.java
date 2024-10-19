package org.telemedicine.server.dto.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String email;
    String accessToken;
    String refreshToken;
    String role;
}
