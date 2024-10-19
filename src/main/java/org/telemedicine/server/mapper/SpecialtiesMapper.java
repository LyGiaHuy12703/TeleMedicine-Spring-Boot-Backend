package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.specialties.SpecialtiesRequest;
import org.telemedicine.server.dto.specialties.SpecialtiesResponse;
import org.telemedicine.server.entity.Specialties;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtiesMapper {
    Specialties toSpecialties(SpecialtiesRequest request);
    SpecialtiesResponse toSpecialtiesResponse(Specialties specialties);
    List<SpecialtiesResponse> toSpecialtiesResponseList(List<Specialties> specialties);
}
