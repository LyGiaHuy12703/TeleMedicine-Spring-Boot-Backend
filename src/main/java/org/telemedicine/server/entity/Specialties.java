package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Specialties {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;

    @OneToMany(mappedBy = "specialties", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<MedicalStaff> medicalStaffs;
}
