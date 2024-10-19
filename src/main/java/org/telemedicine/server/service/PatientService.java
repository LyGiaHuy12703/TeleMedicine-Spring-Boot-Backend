package org.telemedicine.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.telemedicine.server.dto.medicalHistory.MedicalHistoryRequest;
import org.telemedicine.server.dto.medicalHistory.MedicalHistoryResponse;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookRequest;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookResponse;
import org.telemedicine.server.dto.patients.PatientResponse;
import org.telemedicine.server.dto.patients.PatientUpdateRequest;
import org.telemedicine.server.entity.MedicalHistory;
import org.telemedicine.server.entity.MedicalRecordBook;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.MedicalHistoryMapper;
import org.telemedicine.server.mapper.MedicalRecordBookMapper;
import org.telemedicine.server.mapper.PatientMapper;
import org.telemedicine.server.mapper.StaffMapper;
import org.telemedicine.server.repository.MedicalHistoryRepository;
import org.telemedicine.server.repository.MedicalRecordBookRepository;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService {
    PatientRepository patientRepository;
    PatientMapper patientMapper;
    MedicalStaffRepository medicalStaffRepository;
    StaffMapper staffMapper;
    private final MedicalRecordBookMapper medicalRecordBookMapper;
    private final MedicalRecordBookRepository medicalRecordBookRepository;
    private final MedicalHistoryMapper medicalHistoryMapper;
    private final MedicalHistoryRepository medicalHistoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<PatientResponse> getAllPatients() {
        var email = SecurityContextHolder.getContext().getAuthentication();

        log.info(email.getName());
        email.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return patientMapper.toListPatientResponse(patientRepository.findAll());
    }

    public Object getMyInfo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);

        if (patients == null) {
            MedicalStaff medicalStaff = medicalStaffRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "patient not found", "patients-e-01"));

            return staffMapper.toStaffResponse(medicalStaff);
        }

        return patientMapper.toPatientResponse(patients);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public PatientResponse getPatientById(String id) {
        return patientMapper.toPatientResponse(patientRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "patient existed", "patients-e-02")));
    }

    public PatientResponse updateInfoPatient( PatientUpdateRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "patient not found", "patients-e-03");
        }
        patientMapper.toUpdatePatient(patients, request);
        return patientMapper.toPatientResponse(patientRepository.save(patients));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatientById(String id) {
        patientRepository.delete(patientRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "patient not found", "patients-e-01")));
    }

    //medical record book
    public MedicalRecordBookResponse createMedicalRecordBook(MedicalRecordBookRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "patient not found", "medicalRecordBook-e-01");
        }
        MedicalRecordBook medicalRecordBook = medicalRecordBookMapper.toMedicalRecordBook(request);
        medicalRecordBook.setPatients(patients);
        medicalRecordBookRepository.save(medicalRecordBook);
        return medicalRecordBookMapper.toMedicalRecordBookResponse(medicalRecordBook);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MedicalRecordBookResponse getMedicalRecordBookById(String id) {
        MedicalRecordBook medicalRecordBook = medicalRecordBookRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalRecordBook-e-02"));
        return medicalRecordBookMapper.toMedicalRecordBookResponse(medicalRecordBook);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicalRecordBookResponse> getAllMedicalRecordBooks() {
        List<MedicalRecordBook> medicalRecordBooks = medicalRecordBookRepository.findAll();
        if(medicalRecordBooks.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalRecordBook-e-03");
        }
        return medicalRecordBookMapper.toMedicalRecordBookResponses(medicalRecordBooks);
    }
    public MedicalRecordBookResponse getMedicalRecordBook() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "patient not found", "medicalRecordBook-e-04");
        }
        MedicalRecordBook medicalRecordBook = medicalRecordBookRepository.findByPatientId(patients.getId());
        if (medicalRecordBook == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalRecordBook-e-05");
        }
        return medicalRecordBookMapper.toMedicalRecordBookResponse(medicalRecordBook);

    }
    public MedicalRecordBookResponse updateMedicalRecordBook(MedicalRecordBookRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "patient not found", "medicalRecordBook-e-04");
        }
        MedicalRecordBook medicalRecordBook = medicalRecordBookRepository.findByPatientId(patients.getId());
        if (medicalRecordBook == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalRecordBook-e-05");
        }
        medicalRecordBookMapper.toUpdateMedicalRecordBook(request, medicalRecordBook);
        return medicalRecordBookMapper.toMedicalRecordBookResponse(medicalRecordBook);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMedicalRecordBookById(String id) {
        MedicalRecordBook medicalRecordBook = medicalRecordBookRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalRecordBook-e-02"));
        medicalRecordBookRepository.delete(medicalRecordBook);
    }

    //Medical history
    @PreAuthorize("hasRole('ADMIN')")
    public MedicalHistoryResponse createMedicalHistory(MedicalHistoryRequest request) {
        MedicalRecordBook medicalRecordBook = medicalRecordBookRepository.findById(request.getMedicalRecordBookId()).orElse(null);
        if (medicalRecordBook == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalRecordBook-e-01");
        }
        LocalDate date = LocalDate.now();
        MedicalHistory medicalHistory = medicalHistoryMapper.toMedicalHistory(request);
        medicalHistory.setDate(date);
        medicalHistory.setMedicalRecordBook(medicalRecordBook);
        return medicalHistoryMapper.toMedicalHistoryResponse(medicalHistory);
    }
    public List<MedicalHistoryResponse> getMedicalHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "patient not found", "medicalHistory-e-02");
        }
        MedicalRecordBook medicalRecordBook = medicalRecordBookRepository.findByPatientId(patients.getId());
        if (medicalRecordBook == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalRecordBook not found", "medicalHistory-e-03");
        }
        List<MedicalHistory> medicalHistory = medicalHistoryRepository.findAllByMedicalRecordBookId(medicalRecordBook.getId());
        if (medicalHistory.isEmpty()) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalHistory not found", "medicalHistory-e-04");
        }
        return medicalHistoryMapper.toMedicalHistoryResponses(medicalHistory);
    }
    public MedicalHistoryResponse getMedicalHistoryById(String id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "patient not found", "medicalHistory-e-05");
        }
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id).orElse(null);
        if (medicalHistory == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalHistory not found", "medicalHistory-e-06");
        }
        return medicalHistoryMapper.toMedicalHistoryResponse(medicalHistory);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicalHistoryResponse> getAllMedicalHistory() {
        return medicalHistoryMapper.toMedicalHistoryResponses(medicalHistoryRepository.findAll());
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMedicalHistoryById(String id) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id).orElse(null);
        if (medicalHistory == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "medicalHistory not found", "medicalHistory-e-07");
        }
        medicalHistoryRepository.delete(medicalHistory);
    }


}
