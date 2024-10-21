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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonBackReference
    Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "ms_id")
    @JsonBackReference
    MedicalStaff medicalStaff;
}
