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
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int orderNumber;
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    Patients patients;
}
