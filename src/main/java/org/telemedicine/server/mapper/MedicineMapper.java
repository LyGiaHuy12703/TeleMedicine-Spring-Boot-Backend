package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telemedicine.server.dto.medicine.MedicineRequest;
import org.telemedicine.server.dto.medicine.MedicineResponse;
import org.telemedicine.server.entity.Medicine;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    Medicine toMedicine(MedicineRequest request);
    MedicineResponse toMedicineResponse(Medicine medicine);
    List<MedicineResponse> toMedicineResponseList(List<Medicine> medicines);
    Medicine updateMedicine(MedicineRequest request,@MappingTarget Medicine medicine);
}
