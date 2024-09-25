package org.telemedicine.server.entity;

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
    Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "ms_id")
    MedicalStaff medicalStaff;
}
