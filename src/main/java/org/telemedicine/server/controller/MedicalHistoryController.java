package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.medicalHistory.MedicalHistoryRequest;
import org.telemedicine.server.dto.medicalHistory.MedicalHistoryResponse;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookRequest;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookResponse;
import org.telemedicine.server.entity.MedicalHistory;
import org.telemedicine.server.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/lichSuBenh")
public class MedicalHistoryController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<MedicalHistoryResponse>> createMedicalHistory(@RequestBody MedicalHistoryRequest request) {
        ApiResponse<MedicalHistoryResponse> apiResponse = ApiResponse.<MedicalHistoryResponse>builder()
                .data(patientService.createMedicalHistory(request))
                .code("MedicalHistory-s-01")
                .message("MedicalHistory created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/all")
    ResponseEntity<ApiResponse<List<MedicalHistoryResponse>>> getAllMedicalHistory() {
        ApiResponse<List<MedicalHistoryResponse>> apiResponse = ApiResponse.<List<MedicalHistoryResponse>>builder()
                .data(patientService.getAllMedicalHistory())
                .code("MedicalHistory-s-02")
                .message("MedicalHistory get all by admin successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping
    ResponseEntity<ApiResponse<List<MedicalHistoryResponse>>> getMedicalHistoryWithMRB() {
        ApiResponse<List<MedicalHistoryResponse>> apiResponse = ApiResponse.<List<MedicalHistoryResponse>>builder()
                .message("medicalRecordBook get with owner successful ")
                .code("medicalRecordBook-s-03")
                .data(patientService.getMedicalHistory())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("{id}")
    ResponseEntity<ApiResponse<MedicalHistoryResponse>> getMedicalHistoryById(@PathVariable("id") String id) {
        ApiResponse<MedicalHistoryResponse> apiResponse = ApiResponse.<MedicalHistoryResponse>builder()
                .message("MedicalHistory get by admin successful ")
                .code("MedicalHistory-s-04")
                .data(patientService.getMedicalHistoryById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
//    @PutMapping
//    ResponseEntity<ApiResponse<MedicalHistoryResponse>> updateMedicalHistory(@RequestBody MedicalHistoryRequest request) {
//        ApiResponse<MedicalHistoryResponse> apiResponse = ApiResponse.<MedicalHistoryResponse>builder()
//                .message("MedicalHistory update with owner successful ")
//                .code("MedicalHistory-s-05")
//                .data(patientService.updateMedicalHistory(request))
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
    @DeleteMapping("{id}")
    ResponseEntity<ApiResponse<Void>> deleteMedicalHistory(@PathVariable("id") String id) {
        patientService.deleteMedicalHistoryById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete MedicalHistory id: " + id +" success")
                .code("MedicalHistory-s-06")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
