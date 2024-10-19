package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.drug.DrugRequest;
import org.telemedicine.server.dto.drug.DrugResponse;
import org.telemedicine.server.entity.Drug;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DrugMapper {
    Drug toDrug(DrugRequest request);
    DrugResponse toDrugResponse(Drug drug);
    List<DrugResponse> toDrugResponseList(List<Drug> drugs);
}
