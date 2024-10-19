package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telemedicine.server.dto.staff.StaffCreationRequest;
import org.telemedicine.server.dto.staff.StaffUpdateRequest;
import org.telemedicine.server.dto.staff.StaffResponse;
import org.telemedicine.server.entity.MedicalStaff;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    MedicalStaff toStaff(StaffCreationRequest request);
    StaffResponse toStaffResponse(MedicalStaff staff);
    List<StaffResponse> toListStaffResponse(List<MedicalStaff> staffs);
    void toUpdateStaff(@MappingTarget MedicalStaff staff, StaffUpdateRequest request);
}
