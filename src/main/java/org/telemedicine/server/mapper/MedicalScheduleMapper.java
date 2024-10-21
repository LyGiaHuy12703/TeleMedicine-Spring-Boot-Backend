package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleResponse;
import org.telemedicine.server.entity.MedicalSchedule;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalScheduleMapper {
    MedicalScheduleResponse toMedicalScheduleResponse(MedicalSchedule medicalSchedule);
    List<MedicalScheduleResponse> toMedicalScheduleResponses(List<MedicalSchedule> medicalSchedules);
}
