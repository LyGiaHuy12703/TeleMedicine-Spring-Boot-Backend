package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.enums.*;
import org.telemedicine.server.service.EnumService;

import java.util.List;

@RestController
public class EnumController {
    @Autowired
    private EnumService enumService;

    @GetMapping("/roles")
    ResponseEntity<ApiResponse<List<Role>>> getRoles() {
        ApiResponse<List<Role>> apiResponse = ApiResponse.<List<Role>>builder()
                .data(enumService.getRoles())
                .code("roles-s-01")
                .message("Get roles successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/status")
    ResponseEntity<ApiResponse<List<Status>>> getStatusStaff() {
        ApiResponse<List<Status>> apiResponse = ApiResponse.<List<Status>>builder()
                .data(enumService.getStatusStaff())
                .code("status-s-01")
                .message("Get status successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/statusClinic")
    ResponseEntity<ApiResponse<List<StatusClinic>>> getStatusClinic() {
        ApiResponse<List<StatusClinic>> apiResponse = ApiResponse.<List<StatusClinic>>builder()
                .data(enumService.getStatusClinic())
                .code("statusClinic-s-01")
                .message("Get statusClinic successful")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/statusSchedule")
    ResponseEntity<ApiResponse<List<StatusSchedule>>> getStatusSchedule() {
        ApiResponse<List<StatusSchedule>> apiResponse = ApiResponse.<List<StatusSchedule>>builder()
                .data(enumService.getStatusSchedule())
                .code("statusSchedule-s-01")
                .message("Get statusSchedule successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/academicTitle")
    ResponseEntity<ApiResponse<List<AcademicTitle>>> getAcademicTitle() {
        ApiResponse<List<AcademicTitle>> apiResponse = ApiResponse.<List<AcademicTitle>>builder()
                .data(enumService.getAcademicTitle())
                .code("AcademicTitle-s-01")
                .message("Get AcademicTitle successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/academicDegree")
    ResponseEntity<ApiResponse<List<AcademicDegree>>> getAcademicDegree() {
        ApiResponse<List<AcademicDegree>> apiResponse = ApiResponse.<List<AcademicDegree>>builder()
                .data(enumService.getAcademicDegree())
                .code("academicDegree-s-01")
                .message("Get academicDegree successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
