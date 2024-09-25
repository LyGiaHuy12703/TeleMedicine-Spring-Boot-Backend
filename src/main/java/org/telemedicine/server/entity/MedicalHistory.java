package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String medicalHistoryInfo;

    @ManyToOne
    @JoinColumn(name = "medicalRecord_id")
    MedicalRecordBook medicalRecordBook;
}
