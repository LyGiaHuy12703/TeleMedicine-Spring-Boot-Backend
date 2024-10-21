package org.telemedicine.server.dto.prescription;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Examination;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PrescriptionResponse {
    String benh;
    Examination examination;
}
