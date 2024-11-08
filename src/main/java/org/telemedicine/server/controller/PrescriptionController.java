package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.prescription.PrescriptionRequest;
import org.telemedicine.server.dto.prescription.PrescriptionResponse;
import org.telemedicine.server.service.PrescriptionService;

import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<PrescriptionResponse>> create(@RequestBody PrescriptionRequest request){
        ApiResponse<PrescriptionResponse> apiResponse = ApiResponse.<PrescriptionResponse>builder()
                .code("prescription-s-01")
                .message("prescription created successful")
                .data(prescriptionService.create(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getAll(){
        ApiResponse<List<PrescriptionResponse>> apiResponse = ApiResponse.<List<PrescriptionResponse>>builder()
                .code("prescription-s-02")
                .message("prescription get all successful")
                .data(prescriptionService.getAll())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/exam")
    ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getAllByExam(String exam){
        ApiResponse<List<PrescriptionResponse>> apiResponse = ApiResponse.<List<PrescriptionResponse>>builder()
                .code("prescription-s-03")
                .message("prescription get all by exam successful")
                .data(prescriptionService.getAllByExam(exam))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<PrescriptionResponse>> getById(@PathVariable("id") String id){
        ApiResponse<PrescriptionResponse> apiResponse = ApiResponse.<PrescriptionResponse>builder()
                .code("prescription-s-04")
                .message("prescription get by id successful")
                .data(prescriptionService.getById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") String id){
        prescriptionService.delete(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("prescription-s-01")
                .message("prescription created successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
