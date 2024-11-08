package org.telemedicine.server.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.staff.StaffCreationRequest;
import org.telemedicine.server.dto.staff.StaffResponse;
import org.telemedicine.server.dto.staff.StaffUpdateRequest;
import org.telemedicine.server.service.StaffService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @PostMapping("/createStaff")
    ResponseEntity<ApiResponse<StaffResponse>> createStaff(
            @ModelAttribute @Valid StaffCreationRequest request,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var result = staffService.createStaff(request, file);
        ApiResponse<StaffResponse> apiResponse = ApiResponse.<StaffResponse>builder()
                .data(result)
                .code("auth-s-01")
                .message("Create staff successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        ApiResponse<List<StaffResponse>> apiResponse = ApiResponse.<List<StaffResponse>>builder()
                .data(staffService.getAll(page,size))
                .code("auth-s-02")
                .message("Get staff successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable("id") String id) {
        ApiResponse<StaffResponse> apiResponse = ApiResponse.<StaffResponse>builder()
                .data(staffService.getStaffById(id))
                .code("auth-s-03")
                .message("Get staff successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
            @PathVariable("id") String id,
            @ModelAttribute @Valid StaffUpdateRequest request,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        ApiResponse<StaffResponse> apiResponse = ApiResponse.<StaffResponse>builder()
                .data(staffService.updateStaffById(id, request, file))
                .code("auth-s-04")
                .message("Update staff successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteStaffById(@PathVariable("id") String id) {
        staffService.deleteStaffById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-05")
                .message("Enable staff successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
