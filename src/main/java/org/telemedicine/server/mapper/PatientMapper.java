package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telemedicine.server.dto.request.PatientCreationRequest;
import org.telemedicine.server.dto.request.PatientUpdateRequest;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.entity.Patients;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patients toPatient(PatientCreationRequest patient);
    PatientResponse toPatientResponse(Patients patient);
    List<PatientResponse> toListPatientResponse(List<Patients> patients);
    void toUpdatePatient(@MappingTarget Patients patient, PatientUpdateRequest request);
}
