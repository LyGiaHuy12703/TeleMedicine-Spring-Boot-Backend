package org.telemedicine.server.dto.prescription;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PrescriptionRequest {
    String benh;
    String examinationId;
    List<String> medicineIds;
}
