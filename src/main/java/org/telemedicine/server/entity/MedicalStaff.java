package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String fullName;
    String email;
    String password;
    boolean gender;
    Date dob;
    String phone;
    String address;
    Date startDate;
    String practicingCertificate;
    String department;
    String status;

    Set<String> roles = new HashSet<>();//mọi phần tử trong set là unique

    @OneToMany(mappedBy = "medicalStaff", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RoomSchedule> roomSchedules;

    @ManyToOne
    @JoinColumn(name = "specialties_id")
    Specialties specialties;

    @OneToMany(mappedBy = "medicalStaff", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MedicalSchedule> medicalSchedules;

}
