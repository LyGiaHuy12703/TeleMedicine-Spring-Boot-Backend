package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.medicine.MedicineRequest;
import org.telemedicine.server.dto.medicine.MedicineResponse;
import org.telemedicine.server.service.MedicineService;

import java.util.List;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;
    @PostMapping("/create")
    ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(@RequestBody MedicineRequest medicineRequest) {
        ApiResponse<MedicineResponse> apiResponse = ApiResponse.<MedicineResponse>builder()
                .data(medicineService.addMedicine(medicineRequest))
                .code("medicine-s-01")
                .message("medicine created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("drugs/{id}")
    ResponseEntity<ApiResponse<List<MedicineResponse>>> getAllMedicinesByDrugId(@PathVariable("id") String id) {
        ApiResponse<List<MedicineResponse>> apiResponse = ApiResponse.<List<MedicineResponse>>builder()
                .message("medicine with drug id " + id)
                .code("medicine-s-02")
                .data(medicineService.getAllMedicinesByDrugId(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(@PathVariable("id") String id) {
        ApiResponse<MedicineResponse> apiResponse = ApiResponse.<MedicineResponse>builder()
                .message("medicine with id " + id)
                .code("medicine-s-03")
                .data(medicineService.getMedicineById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(@PathVariable("id") String id, @RequestBody MedicineRequest medicineRequest) {
        ApiResponse<MedicineResponse> apiResponse = ApiResponse.<MedicineResponse>builder()
                .message("Update medicine id: " + id +" success")
                .code("medicine-s-04")
                .data(medicineService.updateMedicine(id, medicineRequest))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable("id") String id) {
        medicineService.deleteMedicine(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete Drug id: " + id +" success")
                .code("Drug-s-04")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
