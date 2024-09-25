package org.telemedicine.server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    String id;
    String fullName;
    String email;
    String password;
    boolean gender;
    Date dob;
    String phone;
    String address;
    Date startDate;
    String practicingCertificate;
    String department;
    String status;

    Set<String> roles;
}
