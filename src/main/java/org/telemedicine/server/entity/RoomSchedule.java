package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Date is required")
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    @JsonBackReference
    Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "ms_id", nullable = false)
    @JsonBackReference
    MedicalStaff medicalStaff;
}
