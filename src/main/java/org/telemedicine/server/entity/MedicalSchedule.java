package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Date appointmentDate;
    Time appointmentTime;
    Timestamp appointmentCreateDateTime;
    String status;
    int orderNumber;

    @ManyToOne
    @JoinColumn(name = "ms_id")
    MedicalStaff medicalStaff;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patients patients;
}
