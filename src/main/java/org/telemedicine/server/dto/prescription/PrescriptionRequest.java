package org.telemedicine.server.dto.prescription;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PrescriptionRequest {
    String benh;
    String examination;
}
