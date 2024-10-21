package org.telemedicine.server.dto.roomSchedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.MedicalStaff;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomScheduleResponse {
    String id;
    LocalDate date;
    Clinic clinic;
    MedicalStaff staff;
}
