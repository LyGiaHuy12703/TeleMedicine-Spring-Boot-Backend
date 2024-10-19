package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String benh;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    @JsonBackReference
    Examination examination;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<PrescriptionMedicine> prescriptionMedicines;
}
