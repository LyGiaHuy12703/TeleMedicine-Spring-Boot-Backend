package org.telemedicine.server.dto.medicalRecordBook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.MedicalHistory;
import org.telemedicine.server.entity.Patients;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalRecordBookResponse {
    String fullName;
    boolean gender;
    Date dob;
    String address;
    String phone;
    String bhyt;
    Patients patients;
    List<MedicalHistory> medicalHistory;
}
