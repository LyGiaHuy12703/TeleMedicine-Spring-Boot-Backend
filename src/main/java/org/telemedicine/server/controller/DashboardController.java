package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.enums.StatusSchedule;
import org.telemedicine.server.repository.MedicalScheduleRepository;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.PatientRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalStaffRepository medicalStaffRepository;
    @Autowired
    private MedicalScheduleRepository medicalScheduleRepository;

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getCountDashboard() {
        long countPatients = patientRepository.count();
        long countDoctors = medicalStaffRepository.count();
        long pendingCount = medicalScheduleRepository.countByStatus(StatusSchedule.PENDING);
        long completeCount = medicalScheduleRepository.countByStatus(StatusSchedule.COMPLETED);

        Map<String, Long> counts = new HashMap<>();
        counts.put("patients", countPatients);
        counts.put("doctors", countDoctors);
        counts.put("pending", pendingCount);
        counts.put("complete", completeCount);

        ApiResponse<Map<String, Long>> response = ApiResponse.<Map<String, Long>>builder()
                .code("count-s-01")
                .message("count success")
                .data(counts)
                .build();
        return ResponseEntity.ok(response);
    }
}
