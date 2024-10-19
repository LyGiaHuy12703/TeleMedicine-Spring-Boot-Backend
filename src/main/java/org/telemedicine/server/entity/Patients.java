package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patients {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String fullName;
    String email;
    String password;
    boolean verified = false;

    Set<String> roles = new HashSet<>();

    @OneToMany(mappedBy = "patients", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<MedicalSchedule> medicalSchedule;

    @OneToMany(mappedBy = "patients", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Registration> registration;

    @OneToMany(mappedBy = "patients", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Examination> examination;

    @OneToOne(mappedBy = "patients", cascade = CascadeType.ALL)
    MedicalRecordBook medicalRecordBook;

    @OneToOne(mappedBy = "patients", cascade = CascadeType.ALL)
    Token token;
}
