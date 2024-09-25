package org.telemedicine.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.request.PatientCreationRequest;
import org.telemedicine.server.dto.request.PatientUpdateRequest;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.exception.ErrorCode;
import org.telemedicine.server.mapper.PatientMapper;
import org.telemedicine.server.mapper.StaffMapper;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.PatientRepository;

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
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            return staffMapper.toStaffResponse(medicalStaff);
        }

        return patientMapper.toPatientResponse(patients);
    }

    public PatientResponse getPatientById(String id) {
        return patientMapper.toPatientResponse(patientRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public PatientResponse updateInfoPatient(String email, PatientUpdateRequest request){
        Patients patients = patientRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        patientMapper.toUpdatePatient(patients, request);
        return patientMapper.toPatientResponse(patientRepository.save(patients));
    }
    public void deletePatientByEmail(String email) {
        patientRepository.delete(patientRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
