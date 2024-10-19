package org.telemedicine.server.dto.medicalSchedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;

import java.sql.Time;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalScheduleResponse {
    LocalDate appointmentDate;
    Time appointmentTime;
    LocalDate appointmentCreateDateTime;
    String status;
    int orderNumber;
    MedicalStaff medicalStaff;
    Patients patients;
}
