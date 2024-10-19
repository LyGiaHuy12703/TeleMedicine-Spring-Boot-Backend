package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookRequest;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookResponse;
import org.telemedicine.server.entity.MedicalRecordBook;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalRecordBookMapper {
    MedicalRecordBook toMedicalRecordBook(MedicalRecordBookRequest request);
    MedicalRecordBookResponse toMedicalRecordBookResponse(MedicalRecordBook book);
    List<MedicalRecordBookResponse> toMedicalRecordBookResponses(List<MedicalRecordBook> books);
    MedicalRecordBook toUpdateMedicalRecordBook(MedicalRecordBookRequest request, @MappingTarget MedicalRecordBook book);
}
