package org.telemedicine.server.controller;

import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.clinic.ClinicRequest;
import org.telemedicine.server.dto.clinic.ClinicResponse;
import org.telemedicine.server.service.ClinicService;

import java.util.List;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
    @Autowired
    private ClinicService clinicService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<ClinicResponse>> createClinic(@RequestBody ClinicRequest request) {
        ApiResponse<ClinicResponse> apiResponse = ApiResponse.<ClinicResponse>builder()
                .code("Clinic-s-01")
                .message("Clinic created successful")
                .data(clinicService.createClinic(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping
    ResponseEntity<ApiResponse<List<ClinicResponse>>> getClinics() {
        ApiResponse<List<ClinicResponse>> apiResponse = ApiResponse.<List<ClinicResponse>>builder()
                .code("Clinic-s-02")
                .message("Clinic get by id successful")
                .data(clinicService.getAllClinic())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("{id}")
    ResponseEntity<ApiResponse<ClinicResponse>> getClinic(@PathVariable String id) {
        ApiResponse<ClinicResponse> apiResponse = ApiResponse.<ClinicResponse>builder()
                .code("Clinic-s-03")
                .message("Clinic get by id successful")
                .data(clinicService.getClinicById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping("{id}")
    ResponseEntity<ApiResponse<ClinicResponse>> updateClinic(@PathVariable String id, @RequestBody ClinicRequest request) {
        ApiResponse<ClinicResponse> apiResponse = ApiResponse.<ClinicResponse>builder()
                .code("Clinic-s-04")
                .message("Clinic updated successful")
                .data(clinicService.updateClinicById(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("{id}")
    ResponseEntity<ApiResponse<Void>> deleteClinic(@PathVariable String id) {
        clinicService.deleteClinicById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("Clinic-s-05")
                .message("Clinic deleted successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
