package org.telemedicine.server.controller;

import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleRequest;
import org.telemedicine.server.dto.medicalSchedule.MedicalScheduleResponse;
import org.telemedicine.server.enums.StatusSchedule;
import org.telemedicine.server.service.MedicalScheduleService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/datLich")
public class MedicalScheduleController {
    @Autowired
    private MedicalScheduleService medicalScheduleService;
    //tao lich hen
    @PostMapping("/create")
    ResponseEntity<ApiResponse<MedicalScheduleResponse>> createMedicalSchedule(@RequestBody MedicalScheduleRequest request) {
        ApiResponse<MedicalScheduleResponse> apiResponse = ApiResponse.<MedicalScheduleResponse>builder()
                .code("MedicalSchedule-s-01")
                .message("Medical Schedule created successful")
                .data(medicalScheduleService.createMedicalSchedule(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    //lay lich hen cua ban than
    @GetMapping
    ResponseEntity<ApiResponse<List<MedicalScheduleResponse>>> getYourMedicalSchedules() {
        // Sắp xếp danh sách theo ngày và giờ
        List<MedicalScheduleResponse> schedules = medicalScheduleService.getMyMedicalSchedules();
        schedules.sort(Comparator.comparing(MedicalScheduleResponse::getAppointmentDate)
                .thenComparing(MedicalScheduleResponse::getAppointmentTime));
        ApiResponse<List<MedicalScheduleResponse>> apiResponse = ApiResponse.<List<MedicalScheduleResponse>>builder()
                .code("MedicalSchedule-s-02")
                .message("get all your Medical Schedule successful")
                .data(schedules)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //lay tat ca lich hen theo status
    @GetMapping("/all")
    ResponseEntity<ApiResponse<List<MedicalScheduleResponse>>> getAllMedicalSchedulesByStatus(@RequestParam String status) {
        StatusSchedule statusSchedule;
        try {
            statusSchedule = StatusSchedule.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<MedicalScheduleResponse>>builder()
                            .code("MedicalSchedule-e-01")
                            .message("Invalid status: " + status)
                            .build());
        }

        List<MedicalScheduleResponse> schedules = medicalScheduleService.getMedicalSchedulesByStatus(statusSchedule);
        schedules.sort(Comparator.comparing(MedicalScheduleResponse::getAppointmentDate)
                .thenComparing(MedicalScheduleResponse::getAppointmentTime)
                .thenComparing(MedicalScheduleResponse::getOrderNumber));

        ApiResponse<List<MedicalScheduleResponse>> apiResponse = ApiResponse.<List<MedicalScheduleResponse>>builder()
                .code("MedicalSchedule-s-04")
                .message("Get Medical Schedules by status successful")
                .data(schedules)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //lay lich hen hôm nay
    @GetMapping("/today")
    ResponseEntity<ApiResponse<List<MedicalScheduleResponse>>> getTodayMedicalSchedules() {
        ApiResponse<List<MedicalScheduleResponse>> apiResponse = ApiResponse.<List<MedicalScheduleResponse>>builder()
                .code("MedicalSchedule-s-04")
                .message("get all your Medical Schedule today successful")
                .data(medicalScheduleService.getByToday())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //lay lich hen theo ngay
    @GetMapping("/byDate")
    ResponseEntity<ApiResponse<List<MedicalScheduleResponse>>> getMedicalSchedulesByDate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        ApiResponse<List<MedicalScheduleResponse>> apiResponse = ApiResponse.<List<MedicalScheduleResponse>>builder()
                .code("MedicalSchedule-s-04")
                .message("get all your Medical Schedule successful")
                .data(medicalScheduleService.getByDate(date))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    //lay lich hen theo id
    @GetMapping("{id}")
    ResponseEntity<ApiResponse<MedicalScheduleResponse>> getMedicalSchedule(@PathVariable("id") String id) {
        ApiResponse<MedicalScheduleResponse> apiResponse = ApiResponse.<MedicalScheduleResponse>builder()
                .code("MedicalSchedule-s-04")
                .message("Medical Schedule get successful")
                .data(medicalScheduleService.getMedicalScheduleById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
//    //chinh sua lich hen cua ban than
//    @PutMapping
//    ResponseEntity<ApiResponse<MedicalScheduleResponse>> updateYourMedicalSchedule(@RequestBody MedicalScheduleRequest request) {
//        ApiResponse<MedicalScheduleResponse> apiResponse = ApiResponse.<MedicalScheduleResponse>builder()
//                .code("MedicalSchedule-s-05")
//                .message("Medical Schedule update successful")
//                .data(medicalScheduleService.updateYourMedicalSchedule(request))
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//    }

    //cap nhat status lich hen
    @PutMapping("{id}")
    ResponseEntity<ApiResponse<MedicalScheduleResponse>> updateMedicalSchedule(@PathVariable("id") String id, @RequestBody String status) {
        ApiResponse<MedicalScheduleResponse> apiResponse = ApiResponse.<MedicalScheduleResponse>builder()
                .code("MedicalSchedule-s-06")
                .message("Medical Schedule update status successful")
                .data(medicalScheduleService.updateStatusMedicalSchedule(id, status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //Het han lich hen
//    @PutMapping("{}")

}

