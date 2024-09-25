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
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Date Datetime;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Service service;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patients patients;

    @OneToMany(mappedBy = "examination", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Prescription> prescriptions = new ArrayList<>();

}
