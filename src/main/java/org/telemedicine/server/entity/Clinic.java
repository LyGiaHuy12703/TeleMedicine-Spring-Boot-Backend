package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Clinic name is required")
    @Column(length = 100)
    String name;
    @NotNull(message = "Clinic status is required")
//    @Enumerated(EnumType.STRING) // Chuyển enum sang string trong db
    StatusClinic status;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    List<Examination> examinations;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<RoomSchedule> roomSchedules;
}
