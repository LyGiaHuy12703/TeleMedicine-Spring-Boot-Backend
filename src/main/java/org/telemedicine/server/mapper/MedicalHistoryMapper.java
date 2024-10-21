package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telemedicine.server.dto.medicalHistory.MedicalHistoryRequest;
import org.telemedicine.server.dto.medicalHistory.MedicalHistoryResponse;
import org.telemedicine.server.entity.MedicalHistory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {
    MedicalHistory toMedicalHistory(MedicalHistoryRequest medicalHistory);
    MedicalHistoryResponse toMedicalHistoryResponse(MedicalHistory medicalHistory);
    List<MedicalHistoryResponse> toMedicalHistoryResponses(List<MedicalHistory> medicalHistories);
//    MedicalHistoryResponse toUpdatedMedicalHistory(MedicalHistoryRequest request, @MappingTarget MedicalHistory medicalHistory);
}
