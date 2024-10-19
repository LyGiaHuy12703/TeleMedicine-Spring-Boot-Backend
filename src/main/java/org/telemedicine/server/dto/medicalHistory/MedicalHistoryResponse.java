package org.telemedicine.server.dto.medicalHistory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.MedicalRecordBook;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalHistoryResponse {
    String id;
    String medicalHistoryInfo;
    LocalDate date;
    MedicalRecordBook medicalRecordBook;
}
