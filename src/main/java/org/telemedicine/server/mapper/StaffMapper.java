package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telemedicine.server.dto.request.PatientCreationRequest;
import org.telemedicine.server.dto.request.PatientUpdateRequest;
import org.telemedicine.server.dto.request.StaffCreationRequest;
import org.telemedicine.server.dto.request.StaffUpdateRequest;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.dto.response.StaffResponse;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    MedicalStaff toStaff(StaffCreationRequest request);
    StaffResponse toStaffResponse(MedicalStaff staff);
    List<StaffResponse> toListStaffResponse(List<MedicalStaff> staffs);
    void toUpdateStaff(@MappingTarget MedicalStaff staff, StaffUpdateRequest request);
}
