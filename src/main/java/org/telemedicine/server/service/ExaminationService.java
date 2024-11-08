package org.telemedicine.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.examination.ExaminationRequest;
import org.telemedicine.server.dto.examination.ExaminationResponse;
import org.telemedicine.server.entity.*;
import org.telemedicine.server.enums.StatusSchedule;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.ExaminationMapper;
import org.telemedicine.server.repository.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExaminationService {
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ExaminationMapper examinationMapper;
    @Autowired
    private MedicalScheduleRepository medicalScheduleRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ExaminationResponse create(ExaminationRequest examinationRequest) {
        Clinic clinic = clinicRepository.findById(examinationRequest.getClinicId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "exam-e-01"));
        Patients patients = patientRepository.findById(examinationRequest.getPatientId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Patient not found", "exam-e-02"));
        ServiceEntity service = serviceRepository.findById(examinationRequest.getServiceId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "exam-e-03"));
        MedicalSchedule medicalSchedule = medicalScheduleRepository.findById(examinationRequest.getMedicalScheduleId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Medical schedule not found", "exam-e-04"));

        Examination examination = examinationRepository.findByMedicalSchedule(medicalSchedule);
        examination.setClinic(clinic);
        examination.setPatients(patients);
        examination.setServiceEntity(service);
        examination.setExaminationDate(LocalDate.now());

        return examinationMapper.toExaminationResponse(examinationRepository.save(examination));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExaminationResponse> getAllByClinic(String clinicId, String statusString) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "exam-e-04"));
        StatusSchedule status = null;
        try {
            status = StatusSchedule.valueOf(statusString);  // Chuyển đổi từ String sang enum
        } catch (IllegalArgumentException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid status value", "exam-e-05");
        }
        List<Examination> examinations = examinationRepository.findAllByClinicAndMedicalScheduleStatus(clinic, status);
        return examinationMapper.toExaminationResponses(examinations);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExaminationResponse> getAllByService(String service) {
        ServiceEntity serviceEntity = serviceRepository.findById(service)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "exam-e-05"));
        List<Examination> examinations = examinationRepository.findAllByServiceEntity(serviceEntity);
        return examinationMapper.toExaminationResponses(examinations);
    }
    public List<ExaminationResponse> getAllByPatient(String patient) {
        Patients patients = patientRepository.findById(patient)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Patient not found", "exam-e-06"));
        List<Examination> examinations = examinationRepository.findAllByPatients(patients);
        return examinationMapper.toExaminationResponses(examinations);
    }
    public ExaminationResponse getById(String id) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(()-> new AppException(HttpStatus.NOT_FOUND, "Examination not found", "exam-e-07"));
        return examinationMapper.toExaminationResponse(examination);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(String id) {
        examinationRepository.deleteById(id);
    }
}
