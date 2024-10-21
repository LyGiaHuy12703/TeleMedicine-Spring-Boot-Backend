package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    LocalDate Datetime;

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

}
