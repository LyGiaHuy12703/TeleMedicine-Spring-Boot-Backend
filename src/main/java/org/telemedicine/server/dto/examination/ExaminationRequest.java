package org.telemedicine.server.dto.examination;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExaminationRequest {
    String clinicId;
    String patientId;
    String serviceId;
    String medicalScheduleId;
}
