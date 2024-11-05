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
        if (request.getClinicId() == null || request.getStaffId() == null || request.getDate() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Clinic ID, Staff ID, and date must not be null", "roomSchedule-e-00");
        }

        // Tìm kiếm phòng khám
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng khám", "roomSchedule-e-01"));

        // Tìm kiếm nhân viên y tế
        MedicalStaff staff = medicalStaffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên", "roomSchedule-e-02"));

        // Kiểm tra xem ngày có hợp lệ không
        LocalDate date = request.getDate();
        if (date.isBefore(LocalDate.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Ngày không hợp lệ", "roomSchedule-e-04");
        }

        // Kiểm tra số lượng nhân viên đã đăng ký trong ngày cho phòng khám
        int staffCount = roomScheduleRepository.countByDateAndClinic(date, clinic);
        if (staffCount >= 5) {
            throw new AppException(HttpStatus.CONFLICT, "Chỉ được phép đăng lý tối đa 5 nhân viên cho mỗi phòng trong ngày", "roomSchedule-e-05");
        }

        // Kiểm tra xem nhân viên đã có lịch phòng nào khác trong ngày chưa
        if (roomScheduleRepository.existsByDateAndMedicalStaff(date, staff)) {
            throw new AppException(HttpStatus.CONFLICT, "Nhân viên đã được phân công trực phòng khác", "roomSchedule-e-06");
        }

        // Kiểm tra xem lịch phòng đã tồn tại chưa
        if (roomScheduleRepository.existsByDateAndClinicAndMedicalStaff(date, clinic, staff)) {
            throw new AppException(HttpStatus.CONFLICT, "Lịch trực đã tồn tại", "roomSchedule-e-03");
        }

        // Tạo lịch phòng mới
        RoomSchedule roomSchedule = RoomSchedule.builder()
                .clinic(clinic)
                .date(date)
                .medicalStaff(staff)
                .build();

        // Lưu lịch phòng và trả về kết quả
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
        // Tìm kiếm lịch phòng theo ID
        RoomSchedule roomSchedule = roomScheduleRepository.findById(id).orElse(null);
        if (roomSchedule == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "RoomSchedule not found", "roomSchedule-e-04");
        }

        // Kiểm tra và lấy thông tin phòng khám
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Clinic not found", "roomSchedule-e-01"));

        // Kiểm tra và lấy thông tin nhân viên
        MedicalStaff staff = medicalStaffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "roomSchedule-e-02"));

        // Lấy ngày từ yêu cầu
        LocalDate date = request.getDate();

        // Kiểm tra xem ngày có hợp lệ không (ngày phải từ hôm nay trở đi)
        if (date.isBefore(LocalDate.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Ngày phải từ hôm nay trở đi", "roomSchedule-e-05");
        }

        // Kiểm tra xem lịch phòng có tồn tại cho ngày, phòng khám và nhân viên đó không
        if (roomScheduleRepository.existsByDateAndClinicAndMedicalStaff(date, clinic, staff)) {
            throw new AppException(HttpStatus.CONFLICT, "Lịch phòng đã tồn tại", "roomSchedule-e-03");
        }

        // Kiểm tra số lượng nhân viên đã đăng ký cho phòng trong ngày
        long staffCount = roomScheduleRepository.countByDateAndClinic(date, clinic);
        if (staffCount >= 5) {
            throw new AppException(HttpStatus.CONFLICT, "Phòng chỉ cho phép tối đa 5 nhân viên trong ngày", "roomSchedule-e-06");
        }

        // Cập nhật thông tin cho lịch phòng
        roomSchedule.setClinic(clinic);
        roomSchedule.setMedicalStaff(staff);
        roomSchedule.setDate(date);

        // Lưu và trả về phản hồi
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomScheduleResponse> getAll() {
        List<RoomSchedule> roomSchedules = roomScheduleRepository.findAll();
        return roomScheduleMapper.toRoomScheduleResponses(roomSchedules);
    }
}
