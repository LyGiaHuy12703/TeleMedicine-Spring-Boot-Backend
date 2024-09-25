package org.telemedicine.server.service;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.request.StaffCreationRequest;
import org.telemedicine.server.dto.response.StaffResponse;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.exception.ErrorCode;
import org.telemedicine.server.mapper.StaffMapper;
import org.telemedicine.server.repository.StaffRepository;

import java.util.HashSet;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffMapper staffMapper;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.accessToken}")
    protected String sign_key;

    public StaffResponse createStaff(StaffCreationRequest request) {
        if(staffRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.STAFF_EXISTED);

        MedicalStaff staff = staffMapper.toStaff(request);

        staff.setPassword(passwordEncoder.encode(staff.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(request.getRoles().toString());
        staff.setRoles(roles);
        staffRepository.save(staff);
        return staffMapper.toStaffResponse(staff);
        
    }
}
