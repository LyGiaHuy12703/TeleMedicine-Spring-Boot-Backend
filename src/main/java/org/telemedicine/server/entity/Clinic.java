package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.enums.StatusClinic;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(length = 100)
    String name;
//    @Enumerated(EnumType.STRING) // Chuyển enum sang string trong db
    StatusClinic status;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    List<Examination> examinations;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<RoomSchedule> roomSchedules;
}
