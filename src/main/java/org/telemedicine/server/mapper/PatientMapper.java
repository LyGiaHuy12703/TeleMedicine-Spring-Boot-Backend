package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.patients.PatientResponse;
import org.telemedicine.server.entity.Patients;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponse toPatientResponse(Patients patient);
    List<PatientResponse> toListPatientResponse(List<Patients> patients);
}
