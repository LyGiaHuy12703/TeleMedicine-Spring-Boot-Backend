package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int orderNumber;
    Date date;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patients patients;
}
