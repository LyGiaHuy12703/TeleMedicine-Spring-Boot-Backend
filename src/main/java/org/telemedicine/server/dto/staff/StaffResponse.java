package org.telemedicine.server.dto.staff;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Specialties;
import org.telemedicine.server.enums.Status;

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
    Status status;
    String avatar;
    Set<String> roles;
    boolean isEnabled;
    Specialties specialties;
}
