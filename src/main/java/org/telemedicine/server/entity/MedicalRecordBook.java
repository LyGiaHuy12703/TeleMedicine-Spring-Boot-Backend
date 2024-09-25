package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalRecordBook {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String fullName;
    boolean gender;
    Date dob;
    String address;
    String phone;
    String bhyt;

    @OneToOne
    @JoinColumn(name = "patient_id")
    Patients patients;
//
    @OneToMany(mappedBy = "medicalRecordBook", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MedicalHistory> medicalHistory;
}
