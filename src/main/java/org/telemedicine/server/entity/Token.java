package org.telemedicine.server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String token;
    Date expiryTime;
    @OneToOne(targetEntity = Patients.class, fetch = FetchType.EAGER)
    @JoinColumn( name = "patient_id")
    private Patients patients;
}
