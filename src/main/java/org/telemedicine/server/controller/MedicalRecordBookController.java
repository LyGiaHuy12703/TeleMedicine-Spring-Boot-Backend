package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookRequest;
import org.telemedicine.server.dto.medicalRecordBook.MedicalRecordBookResponse;
import org.telemedicine.server.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/soKhamBenh")
public class MedicalRecordBookController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<MedicalRecordBookResponse>> createMedicalRecordBook(@RequestBody MedicalRecordBookRequest request) {
        ApiResponse<MedicalRecordBookResponse> apiResponse = ApiResponse.<MedicalRecordBookResponse>builder()
                .data(patientService.createMedicalRecordBook(request))
                .code("medicalRecordBook-s-01")
                .message("medicalRecordBook created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/all")
    ResponseEntity<ApiResponse<List<MedicalRecordBookResponse>>> getAllMedicalRecordBook() {
        ApiResponse<List<MedicalRecordBookResponse>> apiResponse = ApiResponse.<List<MedicalRecordBookResponse>>builder()
                .data(patientService.getAllMedicalRecordBooks())
                .code("medicalRecordBook-s-02")
                .message("medicalRecordBook get all by admin successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping
    ResponseEntity<ApiResponse<MedicalRecordBookResponse>> getMedicalRecordBook() {
        ApiResponse<MedicalRecordBookResponse> apiResponse = ApiResponse.<MedicalRecordBookResponse>builder()
                .message("medicalRecordBook get with owner successful ")
                .code("medicalRecordBook-s-03")
                .data(patientService.getMedicalRecordBook())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("{id}")
    ResponseEntity<ApiResponse<MedicalRecordBookResponse>> getMedicalRecordBookById(@PathVariable("id") String id) {
        ApiResponse<MedicalRecordBookResponse> apiResponse = ApiResponse.<MedicalRecordBookResponse>builder()
                .message("medicalRecordBook get by admin successful ")
                .code("medicalRecordBook-s-04")
                .data(patientService.getMedicalRecordBookById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping
    ResponseEntity<ApiResponse<MedicalRecordBookResponse>> updateMedicalRecordBook(@RequestBody MedicalRecordBookRequest request) {
        ApiResponse<MedicalRecordBookResponse> apiResponse = ApiResponse.<MedicalRecordBookResponse>builder()
                .message("medicalRecordBook update with owner successful ")
                .code("medicalRecordBook-s-05")
                .data(patientService.updateMedicalRecordBook(request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("{id}")
    ResponseEntity<ApiResponse<Void>> deleteMedicalRecordBook(@PathVariable("id") String id) {
        patientService.deleteMedicalRecordBookById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete medicalRecordBook id: " + id +" success")
                .code("medicalRecordBook-s-06")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
