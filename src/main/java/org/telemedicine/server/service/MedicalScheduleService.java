package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleRequest;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleResponse;
import org.telemedicine.server.entity.MedicalSchedule;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.enums.StatusSchedule;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.MedicalScheduleMapper;
import org.telemedicine.server.repository.MedicalScheduleRepository;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalScheduleService {
    @Autowired
    private MedicalScheduleRepository medicalScheduleRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalStaffRepository medicalStaffRepository;
    @Autowired
    private MedicalScheduleMapper medicalScheduleMapper;

    public MedicalScheduleResponse createMedicalSchedule(MedicalScheduleRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Patient not found", "medicalSchedule-e-01");
        }
        MedicalStaff staff = medicalStaffRepository.findMedicalStaffById(request.getBs_id());
        if (staff == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Staff not found", "medicalSchedule-e-02");
        }
        List<MedicalSchedule> todaySchedule = medicalScheduleRepository.findByAppointmentDate(LocalDate.now());
        int orderNumber;
        if(todaySchedule.isEmpty()) {
            // Nếu không có bản ghi nào trong ngày, đặt orderNumber là 1
            orderNumber = 1;
        }else{
            // Nếu có, lấy số thứ tự lớn nhất và cộng thêm 1
            orderNumber = todaySchedule.stream()
                    .mapToInt(MedicalSchedule::getOrderNumber)
                    .max()
                    .orElse(0)+1;
        }
        MedicalSchedule medicalSchedule = MedicalSchedule.builder()
                .lyDoKham(request.getLyDoKham())
                .appointmentCreateDateTime(LocalDate.now())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .status(StatusSchedule.PENDING)
                .orderNumber(orderNumber)
                .medicalStaff(staff)
                .patients(patients)
                .build();

        return medicalScheduleMapper.toMedicalScheduleResponse(medicalScheduleRepository.save(medicalSchedule));
    }
    public List<MedicalScheduleResponse> getMyMedicalSchedules() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if (patients == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Patient not found", "medicalSchedule-e-03");
        }
        List<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findByPatientsId(patients.getId());
        return medicalScheduleMapper.toMedicalScheduleResponses(medicalSchedules);
    }
    public MedicalScheduleResponse getMedicalScheduleById(String medicalScheduleId) {
        return medicalScheduleMapper.toMedicalScheduleResponse(medicalScheduleRepository.findById(medicalScheduleId).orElse(null));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicalScheduleResponse> getByDate(LocalDate date) {
        List<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findByAppointmentDate(date);
        return medicalScheduleMapper.toMedicalScheduleResponses(medicalSchedules);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicalScheduleResponse> getAllMedicalSchedules() {
        return medicalScheduleMapper.toMedicalScheduleResponses(medicalScheduleRepository.findAll());
    }


    @PreAuthorize("hasRole('ADMIN')")
    public MedicalScheduleResponse updateStatusMedicalSchedule(String id, String status) {
        MedicalSchedule medicalSchedule = medicalScheduleRepository.findById(id).orElse(null);
        if (medicalSchedule == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "Medical Schedule not found", "medicalSchedule-e-04");
        }
        StatusSchedule statusSchedule = StatusSchedule.valueOf(status);
        medicalSchedule.setStatus(statusSchedule);
        return medicalScheduleMapper.toMedicalScheduleResponse(medicalScheduleRepository.save(medicalSchedule));
    }
}
