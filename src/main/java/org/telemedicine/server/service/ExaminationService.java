package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.examination.ExaminationRequest;
import org.telemedicine.server.dto.examination.ExaminationResponse;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.Examination;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.entity.ServiceEntity;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.ExaminationMapper;
import org.telemedicine.server.repository.ClinicRepository;
import org.telemedicine.server.repository.ExaminationRepository;
import org.telemedicine.server.repository.PatientRepository;
import org.telemedicine.server.repository.ServiceRepository;

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

    @PreAuthorize("hasRole('ADMIN')")
    public ExaminationResponse create(ExaminationRequest examinationRequest) {
        Clinic clinic = clinicRepository.findById(examinationRequest.getClinicId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "exam-e-01"));
        Patients patients = patientRepository.findById(examinationRequest.getPatientId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Patient not found", "exam-e-02"));
        ServiceEntity service = serviceRepository.findById(examinationRequest.getServiceId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Service not found", "exam-e-03"));
        LocalDate date = LocalDate.now();
        Examination examination = Examination.builder()
                .clinic(clinic)
                .serviceEntity(service)
                .patients(patients)
                .Datetime(date)
                .build();
        return examinationMapper.toExaminationResponse(examinationRepository.save(examination));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExaminationResponse> getAllByClinic(String clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "exam-e-04"));
        List<Examination> examinations = examinationRepository.findAllByClinic(clinic);
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
