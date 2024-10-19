package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.telemedicine.server.enums.Status;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String fullName;
    String email;
    String password;
    boolean gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    String phone;
    String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;
    String practicingCertificate;
    String department;
    Status status;
    String avatar;
    boolean isEnabled = true;

    Set<String> roles = new HashSet<>();//mọi phần tử trong set là unique

    @OneToMany(mappedBy = "medicalStaff", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<RoomSchedule> roomSchedules;

    @ManyToOne
    @JoinColumn(name = "specialties_id")
    @JsonBackReference
    Specialties specialties;

    @OneToMany(mappedBy = "medicalStaff", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<MedicalSchedule> medicalSchedules;

}
