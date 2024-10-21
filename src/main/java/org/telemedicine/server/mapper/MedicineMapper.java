package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.medicine.MedicineResponse;
import org.telemedicine.server.entity.Medicine;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    MedicineResponse toMedicineResponse(Medicine medicine);
    List<MedicineResponse> toMedicineResponseList(List<Medicine> medicines);
}
