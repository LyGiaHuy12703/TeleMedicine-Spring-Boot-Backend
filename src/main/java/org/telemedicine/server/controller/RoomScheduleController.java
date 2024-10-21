package org.telemedicine.server.controller;

import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.roomSchedule.RoomScheduleRequest;
import org.telemedicine.server.dto.roomSchedule.RoomScheduleResponse;
import org.telemedicine.server.service.RoomScheduleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/roomSchedule")
public class RoomScheduleController {
    @Autowired
    private RoomScheduleService roomScheduleService;

    @PostMapping("create")
    ResponseEntity<ApiResponse<RoomScheduleResponse>> create(RoomScheduleRequest request) {
        ApiResponse<RoomScheduleResponse> apiResponse = ApiResponse.<RoomScheduleResponse>builder()
                .code("roomSchedule-s-01")
                .message("Room schedule created successful")
                .data(roomScheduleService.create(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
//    gettheo ngay
    @GetMapping("/byDate")
    ResponseEntity<ApiResponse<List<RoomScheduleResponse>>> findByDate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        ApiResponse<List<RoomScheduleResponse>> apiResponse = ApiResponse.<List<RoomScheduleResponse>>builder()
                .code("roomSchedule-s-02")
                .message("Get room schedule by date")
                .data(roomScheduleService.getByDate(date))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<RoomScheduleResponse>> get(@PathVariable("id") String id) {
        ApiResponse<RoomScheduleResponse> apiResponse = ApiResponse.<RoomScheduleResponse>builder()
                .code("roomSchedule-s-02")
                .message("Room schedule get by id successful")
                .data(roomScheduleService.getById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<RoomScheduleResponse>> update(@RequestBody RoomScheduleRequest request, @PathVariable String id) {
        ApiResponse<RoomScheduleResponse> apiResponse = ApiResponse.<RoomScheduleResponse>builder()
                .code("roomSchedule-s-01")
                .message("Room schedule updated by id successful")
                .data(roomScheduleService.updateById(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") String id) {
        roomScheduleService.deleteById(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("roomSchedule-s-01")
                .message("Room schedule deleted successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
