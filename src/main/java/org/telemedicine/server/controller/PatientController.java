package org.telemedicine.server.controller;

import com.cloudinary.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.patients.PatientResponse;
import org.telemedicine.server.dto.patients.PatientUpdateRequest;
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
    ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {
        log.info("in method getAllPatients");
        ApiResponse<List<PatientResponse>> apiResponse = ApiResponse.<List<PatientResponse>>builder()
                .data(patientService.getAllPatients())
                .message("get all patients")
                .code("patient-s-01")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //chỉ lấy được thông tin của chỉnh mình hoặc admin lấy
    @PostAuthorize(value = "returnObject.data.email == authentication.name || hasRole('ADMIN')") //thực hiện method xong thì phân quyền //thực tế ít sử dụng hơn preauthorize
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientByEmail(@PathVariable("id") String id) {
        log.info("in method getPatientByEmail");
        ApiResponse<PatientResponse> apiResponse = ApiResponse.<PatientResponse>builder()
                .data(patientService.getPatientById(id))
                .code("patient-s-02")
                .message("Get patient success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<ApiResponse<Object>> getMyInfo() {
        log.info("in method getPatientByEmail");
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .code("patient-s-03")
                .message("Get my info success")
                .data(patientService.getMyInfo())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient( @RequestBody PatientUpdateRequest request) {
        ApiResponse<PatientResponse> apiResponse = ApiResponse.<PatientResponse>builder()
                .data(patientService.updateInfoPatient(request))
                .code("patient-s-02")
                .message("Get patient success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable("id") String id) {

        patientService.deletePatientById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("patient-s-05")
                .message("Delete patient success")
                .build();
        apiResponse.setMessage("Delete successful");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
