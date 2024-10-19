package org.telemedicine.server.dto.staff;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreationRequest {
    String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;
    boolean gender;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    LocalDate dob;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    String phone;
    @NotBlank(message = "Address is required")
    String address;
    @NotNull(message = "Start date is required")
    LocalDate startDate;
    @NotBlank(message = "Practicing certificate is required")
    String practicingCertificate;
    @NotBlank(message = "Department is required")
    String department;
    @NotBlank(message = "Specialties ID is required")
    String specialtiesId;
    @NotEmpty(message = "At least one role is required")
    Set<String> roles;
}
