package org.telemedicine.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.prescription.PrescriptionRequest;
import org.telemedicine.server.dto.prescription.PrescriptionResponse;
import org.telemedicine.server.entity.Examination;
import org.telemedicine.server.entity.Medicine;
import org.telemedicine.server.entity.Prescription;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.PrescriptionMapper;
import org.telemedicine.server.repository.ExaminationRepository;
import org.telemedicine.server.repository.MedicineRepository;
import org.telemedicine.server.repository.PrescriptionRepository;

import java.util.List;

@Slf4j
@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private PrescriptionMapper prescriptionMapper;
    @Autowired
    private MedicineRepository medicineRepository;

    public PrescriptionResponse getById(String id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Prescription not found", "prescription-e-02"));
        return prescriptionMapper.toPrescriptionResponse(prescription);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public PrescriptionResponse create(PrescriptionRequest request) {
        log.info(request.toString());
        if (request.getExaminationId() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Examination ID cannot be null", "prescription-2-03");
        }

        Examination examination = examinationRepository.findById(request.getExaminationId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Examination not found", "prescription-2-01"));

        if (request.getMedicineIds() == null || request.getMedicineIds().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Medicine IDs cannot be null or empty", "prescription-2-04");
        }

        List<Medicine> medicines = medicineRepository.findAllById(request.getMedicineIds());

        if (medicines.size() != request.getMedicineIds().size()) {
            throw new AppException(HttpStatus.NOT_FOUND, "Some medicines not found", "prescription-2-02");
        }

        Prescription prescription = Prescription.builder()
                .benh(request.getBenh())
                .examination(examination)
                .medicines(medicines)
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
