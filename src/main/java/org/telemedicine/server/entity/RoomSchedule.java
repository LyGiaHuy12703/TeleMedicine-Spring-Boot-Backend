package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class RoomSchedule {
    @Id
    Date date;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonBackReference
    Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "ms_id")
    @JsonBackReference
    MedicalStaff medicalStaff;
}
