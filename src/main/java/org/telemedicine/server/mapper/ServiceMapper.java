package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.service.ServiceRequest;
import org.telemedicine.server.dto.service.ServiceResponse;
import org.telemedicine.server.entity.ServiceEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceEntity toService(ServiceRequest request);
    ServiceResponse toServiceResponse(ServiceEntity response);
    List<ServiceResponse> toServiceResponseList(List<ServiceEntity> responseList);
}
