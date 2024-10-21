package org.telemedicine.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Token is required")
    String token;
    @NotNull(message = "Creation date is required")
    @Column(length = 500)
    String refreshToken;
    Date createAt;
    @OneToOne(targetEntity = MedicalStaff.class, fetch = FetchType.EAGER)
    @JoinColumn( name = "staff_id")
    private MedicalStaff staff;
}
