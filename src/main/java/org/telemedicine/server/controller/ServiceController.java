package org.telemedicine.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.service.ServiceRequest;
import org.telemedicine.server.dto.service.ServiceResponse;
import org.telemedicine.server.service.ServiceService;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(@RequestBody ServiceRequest request) {
        ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
                .data(serviceService.createService(request))
                .code("service-s-01")
                .message("service created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public List<ServiceResponse> getService() {
        return serviceService.getAllsService();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable("id") String id) {
        ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
                .message("service with id " + id)
                .code("service-s-02")
                .data(serviceService.getServiceById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(@PathVariable("id") String id, @RequestBody ServiceRequest request) {
        ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
                .message("Update service id: " + id +" success")
                .code("service-s-03")
                .data(serviceService.updateService(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable("id") String id) {
        serviceService.deleteService(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete service id: " + id +" success")
                .code("service-s-04")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
