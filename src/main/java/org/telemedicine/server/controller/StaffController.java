package org.telemedicine.server.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telemedicine.server.dto.request.ApiResponse;
import org.telemedicine.server.dto.request.StaffCreationRequest;
import org.telemedicine.server.dto.response.StaffResponse;
import org.telemedicine.server.service.StaffService;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @PostMapping("/create")
    public ApiResponse<StaffResponse> createStaff(@RequestBody @Valid StaffCreationRequest request) {
        ApiResponse<StaffResponse> response = new ApiResponse<>();
        response.setData(staffService.createStaff(request));
        return response;
    }
}
