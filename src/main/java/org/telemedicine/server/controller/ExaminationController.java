package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.examination.ExaminationRequest;
import org.telemedicine.server.dto.examination.ExaminationResponse;
import org.telemedicine.server.service.ExaminationService;

import java.util.List;

@RestController
@RequestMapping("/examination")
public class ExaminationController {
    @Autowired
    private ExaminationService examinationService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<ExaminationResponse>> create(@RequestBody ExaminationRequest examinationRequest) {
        ApiResponse<ExaminationResponse> apiResponse = ApiResponse.<ExaminationResponse>builder()
                .code("examination-s-01")
                .message("examination created successful")
                .data(examinationService.create(examinationRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    //lay theo ma phong
    @GetMapping("/clinic")
    ResponseEntity<ApiResponse<List<ExaminationResponse>>> getByClinicId(@RequestParam String clinicId) {
        ApiResponse<List<ExaminationResponse>> apiResponse = ApiResponse.<List<ExaminationResponse>>builder()
                .code("examination-s-02")
                .message("examination get by clinic successful")
                .data(examinationService.getAllByClinic(clinicId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //lay theo ma dich vu
    @GetMapping("/service")
    ResponseEntity<ApiResponse<List<ExaminationResponse>>> getByServiceId(@RequestParam String serviceId) {
        ApiResponse<List<ExaminationResponse>> apiResponse = ApiResponse.<List<ExaminationResponse>>builder()
                .code("examination-s-03")
                .message("examination get by Service successful")
                .data(examinationService.getAllByService(serviceId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //lay theo ma benh nhan
    @GetMapping("/patient")
    ResponseEntity<ApiResponse<List<ExaminationResponse>>> getByPatientId(@RequestParam String patientId) {
        ApiResponse<List<ExaminationResponse>> apiResponse = ApiResponse.<List<ExaminationResponse>>builder()
                .code("examination-s-04")
                .message("examination get by patient successful")
                .data(examinationService.getAllByPatient(patientId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //lay theo id
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ExaminationResponse>> getById(@PathVariable String id) {
        ApiResponse<ExaminationResponse> apiResponse =ApiResponse.<ExaminationResponse>builder()
                .code("examination-s-05")
                .message("examination get by patient successful")
                .data(examinationService.getById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //xoa lan kham
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable String id) {
        examinationService.deleteById(id);
        ApiResponse<Void> apiResponse =ApiResponse.<Void>builder()
                .code("examination-s-06")
                .message("examination delete successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
