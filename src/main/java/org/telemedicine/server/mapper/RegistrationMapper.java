package org.telemedicine.server.mapper;

import org.mapstruct.Mapper;
import org.telemedicine.server.dto.registration.RegistrationRequest;
import org.telemedicine.server.dto.registration.RegistrationResponse;
import org.telemedicine.server.entity.Registration;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    Registration toRegistration(RegistrationRequest registration);
    RegistrationResponse toRegistrationResponse(Registration registration);
}
