package org.telemedicine.server.dto.medicalRecordBook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalRecordBookRequest {
    String fullName;
    boolean gender;
    Date dob;
    String address;
    String phone;
    String bhyt;
}
