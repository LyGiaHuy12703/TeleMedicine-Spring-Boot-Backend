package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.drug.DrugRequest;
import org.telemedicine.server.dto.drug.DrugResponse;
import org.telemedicine.server.service.DrugsService;

@RestController
@RequestMapping("/drugs")
public class DrugsController {
    @Autowired
    private DrugsService drugsService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<DrugResponse>> createDrug(@RequestBody  DrugRequest request){
        ApiResponse<DrugResponse> apiResponse = ApiResponse.<DrugResponse>builder()
                .data(drugsService.createDrugs(request))
                .code("drug-s-01")
                .message("drug created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping
    ApiResponse<Object> getAllDrugs(){
        return ApiResponse.builder()
                .code("drugs-s-01")
                .data(drugsService.getAllDrugs())
                .build();

    }
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<DrugResponse>> getDeugById(@PathVariable("id") String id){
        ApiResponse<DrugResponse> apiResponse = ApiResponse.<DrugResponse>builder()
                .message("Drug with id " + id)
                .code("Drug-s-02")
                .data(drugsService.getDrugById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<DrugResponse>> updateDugById(@PathVariable("id") String id,@RequestBody DrugRequest request){
        ApiResponse<DrugResponse> apiResponse = ApiResponse.<DrugResponse>builder()
                .message("Update Drug id: " + id +" success")
                .code("Drug-s-03")
                .data(drugsService.updateDrugById(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteDrugById(@PathVariable("id") String id){
        drugsService.deleteDrugById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete Drug id: " + id +" success")
                .code("Drug-s-04")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
