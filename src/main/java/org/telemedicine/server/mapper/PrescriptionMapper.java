package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.prescription.PrescriptionResponse;
import org.telemedicine.server.entity.Prescription;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    PrescriptionResponse toPrescriptionResponse(Prescription prescription);
    List<PrescriptionResponse> toPrescriptionResponseList(List<Prescription> prescriptions);
}
