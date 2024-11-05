package org.telemedicine.server.dto.patients;

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
    @Size(min = 5, message = "Tên ít nhất 5 ký tự")
    String fullName;
    @Email(message = "Email không đúng định dạng")
    String email;
    @Size(min = 5, message = "Password ít nhất 5 ký tự")
    String password;
}
