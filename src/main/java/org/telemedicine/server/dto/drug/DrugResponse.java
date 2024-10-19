package org.telemedicine.server.dto.drug;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telemedicine.server.entity.Medicine;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugResponse {
    String id;
    String name;
    List<Medicine> medicine;
}
