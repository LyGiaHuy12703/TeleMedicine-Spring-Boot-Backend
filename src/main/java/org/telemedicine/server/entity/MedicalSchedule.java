package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.enums.StatusSchedule;

import java.sql.Time;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    LocalDate appointmentDate;
    Time appointmentTime;
    LocalDate appointmentCreateDateTime;
    StatusSchedule status;
    int orderNumber;
    String lyDoKham;

    @ManyToOne
    @JoinColumn(name = "ms_id")
    @JsonBackReference
    MedicalStaff medicalStaff;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    Patients patients;
}
