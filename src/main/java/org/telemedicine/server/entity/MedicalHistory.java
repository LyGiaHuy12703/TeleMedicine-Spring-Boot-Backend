package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


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
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "medicalRecord_id")
    @JsonBackReference
    MedicalRecordBook medicalRecordBook;
}
