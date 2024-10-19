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
public class TokenRefresh {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(length = 500)
    String token;
    @Column(length = 500)
    String refreshToken;
    Date createAt;
    @OneToOne(targetEntity = MedicalStaff.class, fetch = FetchType.EAGER)
    @JoinColumn( name = "staff_id")
    private MedicalStaff staff;
}
