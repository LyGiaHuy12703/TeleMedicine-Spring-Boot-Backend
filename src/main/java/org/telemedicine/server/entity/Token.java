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
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotBlank(message = "Token is required")
    @Column(length = 500)
    String token;

    @Column(length = 500)
    String refreshToken;

    @NotNull(message = "Creation date is required")
    Date createAt;

    @OneToOne(targetEntity = Patients.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patients;
}