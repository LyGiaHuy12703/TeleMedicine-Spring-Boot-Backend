package org.telemedicine.server.dto.examination;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.entity.ServiceEntity;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExaminationResponse {
    LocalDate date;
    Clinic clinic;
    ServiceEntity serviceEntity;
    Patients patients;
}
