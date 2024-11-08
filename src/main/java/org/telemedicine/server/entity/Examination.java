package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    LocalDate examinationDate; // Changed to LocalDateTime for date and time

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference
    ServiceEntity serviceEntity;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonBackReference
    Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    Patients patients;

    @OneToMany(mappedBy = "examination", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Prescription> prescriptions = new ArrayList<>();

    @OneToOne @JoinColumn(name = "medical_schedule_id", nullable = false)
    @JsonBackReference
    MedicalSchedule medicalSchedule;
}
