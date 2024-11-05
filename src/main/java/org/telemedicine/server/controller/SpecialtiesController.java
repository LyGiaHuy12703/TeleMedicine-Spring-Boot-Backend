package org.telemedicine.server.controller;

import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.specialties.SpecialtiesRequest;
import org.telemedicine.server.dto.specialties.SpecialtiesResponse;
import org.telemedicine.server.service.SpecialtiesService;

import java.util.List;

@RestController
@RequestMapping("/specialties")
public class SpecialtiesController {
    @Autowired
    private SpecialtiesService specialtiesService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<SpecialtiesResponse>> createSpecialties(@RequestBody SpecialtiesRequest specialtiesRequest) {
        ApiResponse<SpecialtiesResponse> apiResponse = ApiResponse.<SpecialtiesResponse>builder()
                .data(specialtiesService.createSpecialties(specialtiesRequest))
                .code("specialties-s-01")
                .message("Specialties created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<SpecialtiesResponse>>> getSpecialties() {
        ApiResponse<List<SpecialtiesResponse>> apiResponse = ApiResponse.<List<SpecialtiesResponse>>builder()
                .message("get all Specialties success  " )
                .code("specialties-s-02")
                .data(specialtiesService.getAllSpecialties())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/medicalStaffs")
    ResponseEntity<ApiResponse<List<SpecialtiesResponse>>> getSpecialtiesWithMedicalStaffs() {
        ApiResponse<List<SpecialtiesResponse>> apiResponse = ApiResponse.<List<SpecialtiesResponse>>builder()
                .message("get all Specialties success  " )
                .code("specialties-s-02")
                .data(specialtiesService.getSpecialtiesWithMedicalStaffs())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecialtiesResponse>> getSpecialtiesById(@PathVariable("id") String id) {
        ApiResponse<SpecialtiesResponse> apiResponse = ApiResponse.<SpecialtiesResponse>builder()
                .message("Specialties with id " + id)
                .code("specialties-s-02")
                .data(specialtiesService.getSpecialtiesById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SpecialtiesResponse>> updateSpecialties(@PathVariable("id") String id, @RequestBody SpecialtiesRequest specialtiesRequest) {
        ApiResponse<SpecialtiesResponse> apiResponse = ApiResponse.<SpecialtiesResponse>builder()
                .message("Update Specialties id: " + id +" success")
                .code("specialties-s-03")
                .data(specialtiesService.updateSpecialtiesById(id, specialtiesRequest))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSpecialties(@PathVariable("id") String id) {
        specialtiesService.deleteSpecialtiesById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete specialties id: " + id +" success")
                .code("specialties-s-04")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
