package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalRecordBook {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    String fullName;

    boolean gender;

    @Temporal(TemporalType.DATE) // Ensures only the date is stored, not the time
    Date dob;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    String address;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    String phone;

    String bhyt; // Optional: Can add validation if needed

    @OneToOne
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    Patients patients;

    @OneToMany(mappedBy = "medicalRecordBook", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<MedicalHistory> medicalHistory;
}
