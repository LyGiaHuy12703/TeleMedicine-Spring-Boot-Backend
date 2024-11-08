package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.telemedicine.server.dto.clinic.ClinicRequest;
import org.telemedicine.server.dto.clinic.ClinicResponse;
import org.telemedicine.server.entity.Clinic;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    Clinic toClinic(ClinicRequest clinic);
    ClinicResponse toClinicResponse(Clinic clinic);
    List<ClinicResponse> toClinicResponses(Page<Clinic> clinics);
}
