package org.telemedicine.server.dto.prescription;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Examination;
import org.telemedicine.server.entity.Medicine;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PrescriptionResponse {
    String benh;
    Examination examination;
    List<Medicine> medicines;

}
