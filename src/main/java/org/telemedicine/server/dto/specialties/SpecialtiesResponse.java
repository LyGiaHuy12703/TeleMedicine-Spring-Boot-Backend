package org.telemedicine.server.dto.specialties;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.MedicalStaff;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecialtiesResponse {
    String id;
    String name;
    List<MedicalStaff> medicalStaffs;
}
