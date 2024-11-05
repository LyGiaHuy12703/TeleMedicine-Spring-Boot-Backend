package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.registration.RegistrationResponse;
import org.telemedicine.server.service.RegistrationService;

import java.util.List;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<RegistrationResponse>> createDrug(){
        ApiResponse<RegistrationResponse> apiResponse = ApiResponse.<RegistrationResponse>builder()
                .data(registrationService.createRegistration())
                .code("registration-s-01")
                .message("registration created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
//    @GetMapping
//    List<DrugResponse> getAllDrugs(){
//        return registrationService.getAllRegistration();
//    }
    @GetMapping()
    ResponseEntity<ApiResponse<RegistrationResponse>> getById(){
        ApiResponse<RegistrationResponse> apiResponse = ApiResponse.<RegistrationResponse>builder()
                .message("Get registration of user success " )
                .code("registration-s-02")
                .data(registrationService.getRegistrationById())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
//    @PutMapping("/{id}")
//    ResponseEntity<ApiResponse<RegistrationResponse>> updateDugById(@PathVariable("id") String id, DrugRequest request){
//        ApiResponse<RegistrationResponse> apiResponse = ApiResponse.<RegistrationResponse>builder()
//                .message("Update registration id: " + id +" success")
//                .code("registration-s-03")
//                .data(registrationService.updateRegistration(id, request))
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
//    @DeleteMapping("/{id}")
//    ResponseEntity<ApiResponse<Void>> deleteDrugById(@PathVariable("id") String id){
//        registrationService.deleteRegistration(id);
//        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
//                .message("Delete registration id: " + id +" success")
//                .code("registration-s-04")
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }
}
