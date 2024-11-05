package org.telemedicine.server.dto.staff;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffUpdateRequest {
    String fullName;
    String password;
    boolean gender;
    LocalDate dob;
    String phone;
    String address;
    LocalDate startDate;
    String practicingCertificate;
    List<String> specialtiesId;
    String status;
    Set<String> roles;
    Set<String> hocHam;
    Set<String> hocVi;
    boolean isEnable;
}
