package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleRequest;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleResponse;
import org.telemedicine.server.entity.MedicalSchedule;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalScheduleMapper {
    MedicalSchedule toMedicalSchedule(MedicalScheduleRequest request);
    MedicalScheduleResponse toMedicalScheduleResponse(MedicalSchedule medicalSchedule);
    List<MedicalScheduleResponse> toMedicalScheduleResponses(List<MedicalSchedule> medicalSchedules);
    MedicalScheduleResponse toUpdateMedicalScheduleResponse(MedicalSchedule medicalSchedule);
}
