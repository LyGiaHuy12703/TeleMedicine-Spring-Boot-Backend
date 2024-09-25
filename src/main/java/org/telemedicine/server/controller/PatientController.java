package org.telemedicine.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.request.ApiResponse;
import org.telemedicine.server.dto.request.PatientUpdateRequest;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.service.PatientService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PreAuthorize(value = "hasRole('ADMIN')") // phân quyền trước khi vào method
    @GetMapping
    public ApiResponse<List<PatientResponse>> getAllPatients() {
        log.info("in method getAllPatients");
        ApiResponse<List<PatientResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(patientService.getAllPatients());
        return apiResponse;
    }
    //chỉ lấy được thông tin của chỉnh mình hoặc admin lấy
    @PostAuthorize(value = "returnObject.data.email == authentication.name || hasRole('ADMIN')") //thực hiện method xong thì phân quyền //thực tế ít sử dụng hơn preauthorize
    @GetMapping("/{id}")
    public ApiResponse<PatientResponse> getPatientByEmail(@PathVariable("id") String id) {
        log.info("in method getPatientByEmail");
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(patientService.getPatientById(id));
        return apiResponse;
    }

    @GetMapping("/myInfo")
    public ApiResponse<Object> getMyInfo() {
        log.info("in method getPatientByEmail");
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setData(patientService.getMyInfo());
        return apiResponse;
    }

    @PutMapping("/{email}")
    public ApiResponse<PatientResponse> updatePatient(@PathVariable("email") String email, @RequestBody PatientUpdateRequest request) {
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(patientService.updateInfoPatient(email, request));
        return apiResponse;
    }
    @DeleteMapping("/{email}")
    public ApiResponse deletePatient(@PathVariable("email") String email) {
        ApiResponse apiResponse = new ApiResponse();
        patientService.deletePatientByEmail(email);
        apiResponse.setMessage("Delete successful");
        return apiResponse;
    }
}
