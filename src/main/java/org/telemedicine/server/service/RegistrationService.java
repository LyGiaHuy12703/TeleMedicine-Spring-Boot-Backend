package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.registration.RegistrationRequest;
import org.telemedicine.server.dto.registration.RegistrationResponse;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.entity.Registration;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.RegistrationMapper;
import org.telemedicine.server.repository.PatientRepository;
import org.telemedicine.server.repository.RegistrationRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private RegistrationMapper registrationMapper;

    @PreAuthorize("hasRole('USER')")
    public RegistrationResponse createRegistration() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "patient-e-01"));
        LocalDate currentDate = LocalDate.now();
        List<Registration> todayRegistrations = registrationRepository.findByDate(currentDate);

        int orderNumber;
        if(todayRegistrations.isEmpty()) {
            // Nếu không có bản ghi nào trong ngày, đặt orderNumber là 1
            orderNumber = 1;
        }else{
            // Nếu có, lấy số thứ tự lớn nhất và cộng thêm 1
            orderNumber = todayRegistrations.stream()
                    .mapToInt(Registration::getOrderNumber)
                    .max()
                    .orElse(0)+1;
        }
        Registration registration = Registration.builder()
                .date(currentDate)
                .orderNumber(orderNumber)
                .patients(patient)
                .build();

        return registrationMapper.toRegistrationResponse(registrationRepository.save(registration));
    }
    @PreAuthorize("hasRole('USER')")
    public RegistrationResponse getRegistrationById(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "patient-e-01"));

        Registration registration = registrationRepository.findFirstByPatientsIdAndDateDesc(patient.getId()).orElse(null);
        if(registration == null){
            throw new AppException(HttpStatus.NOT_FOUND, "User not found", "patient-e-01");
        }
        return registrationMapper.toRegistrationResponse(registration);
    }

}
