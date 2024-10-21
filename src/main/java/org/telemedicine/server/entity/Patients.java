package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(length = 100)
    String fullName;
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true, length = 100)
    String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(length = 255)
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
