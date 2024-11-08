package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.clinic.ClinicRequest;
import org.telemedicine.server.dto.clinic.ClinicResponse;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.Examination;
import org.telemedicine.server.entity.RoomSchedule;
import org.telemedicine.server.enums.StatusClinic;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.ClinicMapper;
import org.telemedicine.server.repository.ClinicRepository;
import org.telemedicine.server.repository.ExaminationRepository;
import org.telemedicine.server.repository.RoomScheduleRepository;

import java.util.List;

@Service
public class ClinicService {
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private ClinicMapper clinicMapper;
    @Autowired
    private RoomScheduleRepository roomScheduleRepository;
    @Autowired
    private ExaminationRepository examinationRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ClinicResponse createClinic(ClinicRequest clinicRequest) {
        if(clinicRepository.existsByName(clinicRequest.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Clinic name existed", "clinic-e-01");
        }
        StatusClinic statusClinic = StatusClinic.valueOf(clinicRequest.getStatus());
        Clinic clinic = clinicMapper.toClinic(clinicRequest);
        clinic.setStatus(statusClinic);
        clinicRepository.save(clinic);
        return clinicMapper.toClinicResponse(clinic);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClinicResponse> getAllClinic(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);

        Page<Clinic> clinics = clinicRepository.findAll(pageable);
        return clinicMapper.toClinicResponses(clinics);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicResponse getClinicById(String id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "clinic-e-02"));
        return clinicMapper.toClinicResponse(clinic);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicResponse updateClinicById(String id, ClinicRequest clinicRequest) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "clinic-e-03"));

        clinic.setName(clinicRequest.getName());
        StatusClinic statusClinic = StatusClinic.valueOf(clinicRequest.getStatus());
        clinic.setStatus(statusClinic);
        clinicRepository.save(clinic);
        return clinicMapper.toClinicResponse(clinic);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClinicById(String id) {
        if(!clinicRepository.existsById(id)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Clinic not found", "clinic-e-04");
        }

        RoomSchedule roomSchedule = roomScheduleRepository.findByClinicId(id);
        if (roomSchedule != null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Không thể xóa phòng khám vì đã được sắp lịch", "clinic-e-05");
        }
        Examination examination = examinationRepository.findByClinicId(id);
        if(examination != null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Không thể xóa phòng khám vì đã có lần khám được lưu tại phòng", "clinic-e-06");
        }
        clinicRepository.deleteById(id);
    }
}
