package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.prescriptionMedicine.PrescriptionMedicineRequest;
import org.telemedicine.server.dto.prescriptionMedicine.PrescriptionMedicineResponse;
import org.telemedicine.server.service.PrescriptionMedicineService;

@RestController
@RequestMapping("/preMedicine")
public class PrescriptionMedicineController {
    @Autowired
    private PrescriptionMedicineService prescriptionMedicineService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<PrescriptionMedicineResponse>> create(PrescriptionMedicineRequest request){
        ApiResponse<PrescriptionMedicineResponse> apiResponse = ApiResponse.<PrescriptionMedicineResponse>builder()
                .code("prescriptionMedicine-s-01")
                .message("prescriptionMedicine created successful")
                .data(prescriptionMedicineService.create(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
//
//    @GetMapping
//    ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getAll(){
//
//    }
//    @GetMapping("/{id}")
//    ResponseEntity<ApiResponse<PrescriptionResponse>> getById(@PathVariable("id") String id){
//
//    }
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") String id){
        prescriptionMedicineService.delete(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("prescriptionMedicine-s-02")
                .message("prescriptionMedicine deleted successful")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }
}
