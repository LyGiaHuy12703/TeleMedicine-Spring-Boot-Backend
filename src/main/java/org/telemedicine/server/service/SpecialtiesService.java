package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.specialties.SpecialtiesRequest;
import org.telemedicine.server.dto.specialties.SpecialtiesResponse;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Specialties;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.SpecialtiesMapper;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.SpecialtiesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtiesService {
    @Autowired
    private SpecialtiesRepository specialtiesRepository;
    @Autowired
    private SpecialtiesMapper specialtiesMapper;
    @Autowired
    private MedicalStaffRepository medicalStaffRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public SpecialtiesResponse createSpecialties(SpecialtiesRequest specialtiesRequest) {
        if(specialtiesRepository.existsByName(specialtiesRequest.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST,"Specialties already exist", "specialties-e-01");
        };
        Specialties specialties = specialtiesMapper.toSpecialties(specialtiesRequest);
        specialtiesRepository.save(specialties);
        return specialtiesMapper.toSpecialtiesResponse(specialties);
    }
    public List<SpecialtiesResponse> getAllSpecialties() {
        List<Specialties> specialties = specialtiesRepository.findAll();
        return specialtiesMapper.toSpecialtiesResponseList(specialties);
    }
    public SpecialtiesResponse getSpecialtiesById(String specialtiesId) {
        Specialties specialties = specialtiesRepository.findById(specialtiesId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Specialties not found", "specialties-e-02"));

        return specialtiesMapper.toSpecialtiesResponse(specialties);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public SpecialtiesResponse updateSpecialtiesById(String id, SpecialtiesRequest specialtiesRequest) {
        Specialties specialties = specialtiesRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Specialties not found", "specialties-e-02"));

        specialties.setName(specialtiesRequest.getName());
        specialtiesRepository.save(specialties);
        return specialtiesMapper.toSpecialtiesResponse(specialties);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSpecialtiesById(String id) {
        specialtiesRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Specialties not found", "specialties-e-02"));
        List<MedicalStaff> staff = medicalStaffRepository.findMedicalStaffBySpecialtiesId(id);
        if(!staff.isEmpty()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Không thể xoá chuyên ngành ", "specialties-e-03");
        }
        specialtiesRepository.deleteById(id);
    }

}
