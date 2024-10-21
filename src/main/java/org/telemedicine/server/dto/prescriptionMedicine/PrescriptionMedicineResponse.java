package org.telemedicine.server.dto.prescriptionMedicine;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Medicine;
import org.telemedicine.server.entity.Prescription;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PrescriptionMedicineResponse {
    String id;
    Medicine medicine;
    Prescription prescription;
}
