package org.telemedicine.server.dto.medicalSchedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalScheduleRequest {
    LocalDate appointmentDate;
    Time appointmentTime;
    String lyDoKham;
    String bs_id;
}
