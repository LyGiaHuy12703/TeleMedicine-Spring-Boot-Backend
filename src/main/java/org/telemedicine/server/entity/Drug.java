package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @NotBlank(message = "Medicine name is required")
    @Column(length = 100)
    String name;

    @OneToMany(mappedBy = "drug", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Medicine> medicine;
}
