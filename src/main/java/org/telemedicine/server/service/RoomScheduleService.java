package org.telemedicine.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.roomSchedule.RoomScheduleRequest;
import org.telemedicine.server.dto.roomSchedule.RoomScheduleResponse;
import org.telemedicine.server.entity.Clinic;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.RoomSchedule;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.RoomScheduleMapper;
import org.telemedicine.server.repository.ClinicRepository;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.RoomScheduleRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomScheduleService {
    @Autowired
    private RoomScheduleRepository roomScheduleRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private MedicalStaffRepository medicalStaffRepository;
    @Autowired
    private RoomScheduleMapper roomScheduleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public RoomScheduleResponse create(RoomScheduleRequest request) {
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "roomSchedule-e-01"));
        MedicalStaff staff = medicalStaffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "roomSchedule-e-02"));
        LocalDate date = request.getDate();
        if(roomScheduleRepository.existsByDateAndClinicAndMedicalStaff(date, clinic.getId(), staff.getId())) {
            throw new AppException(HttpStatus.CONFLICT, "Lịch phòng đã tồn tại", "roomSchedule-e-03");
        }
        RoomSchedule roomSchedule = RoomSchedule.builder()
                .clinic(clinic)
                .date(date)
                .medicalStaff(staff)
                .build();
        return roomScheduleMapper.toRoomScheduleResponse(roomScheduleRepository.save(roomSchedule));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomScheduleResponse> getByDate(LocalDate date) {
        List<RoomSchedule> roomSchedules = roomScheduleRepository.findByDate(date);
        if(roomSchedules.isEmpty()) {
            throw new AppException(HttpStatus.NOT_FOUND, "RoomSchedule not found", "roomSchedule-e-04");
        }
        return roomScheduleMapper.toRoomScheduleResponses(roomSchedules);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public RoomScheduleResponse getById(String id) {
        RoomSchedule roomSchedule = roomScheduleRepository.findById(id).orElse(null);
        if (roomSchedule == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "RoomSchedule not found", "roomSchedule-e-04");
        }
        return roomScheduleMapper.toRoomScheduleResponse(roomSchedule);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public RoomScheduleResponse updateById(String id, RoomScheduleRequest request) {
        RoomSchedule roomSchedule = roomScheduleRepository.findById(id).orElse(null);
        if (roomSchedule == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "RoomSchedule not found", "roomSchedule-e-04");
        }
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "roomSchedule-e-01"));
        MedicalStaff staff = medicalStaffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "roomSchedule-e-02"));
        LocalDate date = request.getDate();
        if(roomScheduleRepository.existsByDateAndClinicAndMedicalStaff(date, clinic.getId(), staff.getId())) {
            throw new AppException(HttpStatus.CONFLICT, "Lịch phòng đã tồn tại", "roomSchedule-e-03");
        }
        roomSchedule.setClinic(clinic);
        roomSchedule.setMedicalStaff(staff);
        roomSchedule.setDate(date);
        return roomScheduleMapper.toRoomScheduleResponse(roomScheduleRepository.save(roomSchedule));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(String id) {
        RoomSchedule roomSchedule = roomScheduleRepository.findById(id).orElse(null);
        if (roomSchedule == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "RoomSchedule not found", "roomSchedule-e-04");
        }
        roomScheduleRepository.delete(roomSchedule);
    }
}
