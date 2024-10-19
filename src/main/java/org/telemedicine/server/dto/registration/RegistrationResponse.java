package org.telemedicine.server.dto.registration;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Patients;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RegistrationResponse {
    int orderNumber;
    LocalDate date;
    Patients patients;
}
