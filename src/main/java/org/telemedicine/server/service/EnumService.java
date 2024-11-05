package org.telemedicine.server.service;

import org.springframework.stereotype.Service;
import org.telemedicine.server.enums.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnumService {
    public List<Role> getRoles() {
        return Arrays.stream(Role.values())
                .filter(role -> role != Role.USER) // Exclude the USER role
                .filter(role -> role != Role.ADMIN)
                .collect(Collectors.toList());
    }

    public List<Status> getStatusStaff() {
        return Arrays.asList(Status.values());
    }

    public List<StatusClinic> getStatusClinic() {
        return Arrays.asList(StatusClinic.values());
    }

    public List<StatusSchedule> getStatusSchedule() {
        return Arrays.asList(StatusSchedule.values());
    }
    public List<AcademicTitle> getAcademicTitle() {
        return Arrays.asList(AcademicTitle.values());
    }
    public List<AcademicDegree> getAcademicDegree() {
        return Arrays.asList(AcademicDegree.values());
    }

}
