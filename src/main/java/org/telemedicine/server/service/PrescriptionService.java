package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.prescription.PrescriptionRequest;
import org.telemedicine.server.dto.prescription.PrescriptionResponse;
import org.telemedicine.server.entity.Examination;
import org.telemedicine.server.entity.Prescription;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.PrescriptionMapper;
import org.telemedicine.server.repository.ExaminationRepository;
import org.telemedicine.server.repository.PrescriptionRepository;

import java.util.List;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private PrescriptionMapper prescriptionMapper;

    public PrescriptionResponse getById(String id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Prescription not found", "prescription-e-02"));
        return prescriptionMapper.toPrescriptionResponse(prescription);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public PrescriptionResponse create(PrescriptionRequest request) {
        Examination examination = examinationRepository.findById(request.getExamination())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Examination not found", "prescription-2-01"));

        Prescription prescription = Prescription.builder()
                .benh(request.getBenh())
                .examination(examination)
                .build();
        return prescriptionMapper.toPrescriptionResponse(prescriptionRepository.save(prescription));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<PrescriptionResponse> getAll() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptionMapper.toPrescriptionResponseList(prescriptions);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<PrescriptionResponse> getAllByExam(String exam) {
        List<Prescription> prescriptions = prescriptionRepository.findAllByExaminationId(exam);
        return prescriptionMapper.toPrescriptionResponseList(prescriptions);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Prescription not found", "prescription-e-03"));

        prescriptionRepository.delete(prescription);
    }
}
