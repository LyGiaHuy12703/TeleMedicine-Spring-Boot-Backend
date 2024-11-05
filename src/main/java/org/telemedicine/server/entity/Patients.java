package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.enums.Role;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patients {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String fullName;
    @Column(unique = true)
    String email;

    String password;

    Role role;

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
    @JsonManagedReference
    MedicalRecordBook medicalRecordBook;

    @OneToOne(mappedBy = "patients", cascade = CascadeType.ALL)
    @JsonIgnore
    Token token;
}
