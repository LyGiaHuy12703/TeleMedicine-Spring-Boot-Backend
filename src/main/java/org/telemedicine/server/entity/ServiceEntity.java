package org.telemedicine.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String type;
    String price;

    @OneToMany(mappedBy = "serviceEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Examination> examinations;



}
