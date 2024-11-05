package org.telemedicine.server.service;

import com.cloudinary.utils.ObjectUtils;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telemedicine.server.configuration.CloudinaryConfig;
import org.telemedicine.server.dto.staff.StaffCreationRequest;
import org.telemedicine.server.dto.staff.StaffResponse;
import org.telemedicine.server.dto.staff.StaffUpdateRequest;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Specialties;
import org.telemedicine.server.enums.Status;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.StaffMapper;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.SpecialtiesRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StaffService {
    @Autowired
    private MedicalStaffRepository staffRepository;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CloudinaryConfig cloudinary;

    @NonFinal
    @Value("${jwt.accessToken}")
    protected String sign_key;
    @Autowired
    private SpecialtiesRepository specialtiesRepository;

    //tạo tài khoản nhân viên
    @PreAuthorize("hasRole('ADMIN')")
    public StaffResponse createStaff(
            StaffCreationRequest request, MultipartFile file) throws IOException {
        if(staffRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Email existed", "auth-e-03");

        String avatarUrl = uploadImg(file, request.getEmail());
        log.warn(avatarUrl);
        MedicalStaff staff = MedicalStaff.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .dob(request.getDob())
                .phone(request.getPhone())
                .gender(request.isGender())
                .startDate(request.getStartDate())
                .practicingCertificate(request.getPracticingCertificate())
                .avatar(avatarUrl)
                .build();

        // Lưu các chuyên khoa
        Set<Specialties> specialties = new HashSet<>();
        for (String specialtyId : request.getSpecialtiesId()) {
            Specialties specialty = specialtiesRepository.findById(specialtyId)
                    .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Specialty not found: " + specialtyId, "specialty-not-found"));
            specialties.add(specialty);
            specialty.getStaffs().add(staff); // Thiết lập quan hệ hai chiều
        }
        staff.setSpecialties(specialties);
        staff.setSpecialties(specialties);
        HashSet<String> roles = new HashSet<>(request.getRoles());

        HashSet<String> hocHam = new HashSet<>(request.getHocHam());
        staff.setHocHam(hocHam);

        HashSet<String> hocVi = new HashSet<>(request.getHocVi());
        staff.setHocVi(hocVi);

        staff.setRoles(roles);
        staff.setStatus(Status.ACTIVE);
        staff.setEnabled(true);
        staffRepository.save(staff);
        return staffMapper.toStaffResponse(staff);
    }

    @PreAuthorize("hasRole('ADMIN')")
    private String uploadImg(MultipartFile file, String email) throws IOException {
        if(staffRepository.existsByEmail(email)){
            throw new AppException(HttpStatus.BAD_REQUEST, "User not found", "Upload-e-01");
        }
        var result = cloudinary.cloudinary().uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "teleMedicine/"+email+"/avatar"
                ));
        return (String) result.get("secure_url");
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Map deleteImg(String url){
        String path = extractPublicIdFromUrl(url);
        if(path == null){
            return Map.of("Error", "Invalid format error");
        }
        try{
            Map<String, Object> result = cloudinary.cloudinary().uploader().destroy(path, ObjectUtils.emptyMap());
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String extractPublicIdFromUrl(String url) {
        // Tìm vị trí bắt đầu của "teleMedicine"
        int startIndex = url.indexOf("teleMedicine");

        // Tìm vị trí của dấu chấm (phần mở rộng)
        int endIndex = url.indexOf(".", startIndex);

        // Kiểm tra xem cả hai vị trí có hợp lệ không
        if (startIndex != -1 && endIndex != -1) {
            // Trả về dữ liệu từ "teleMedicine" đến trước phần mở rộng
            return url.substring(startIndex, endIndex);
        }

        return null; // Trả về null nếu không tìm thấy
    }

    public List<StaffResponse> getAll() {
        List<MedicalStaff> allStaff = staffRepository.findAll();

        // Lọc ra những staff không có role là ADMIN
        List<MedicalStaff> filteredStaff = allStaff.stream()
                .filter(staff -> !staff.getRoles().contains ("ADMIN")) // Giả sử bạn có phương thức getRole() trong Staff
                .collect(Collectors.toList());

        return staffMapper.toListStaffResponse(filteredStaff);
    }

    public StaffResponse getStaffById(String id){
        return staffMapper.toStaffResponse(staffRepository.findMedicalStaffById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public StaffResponse updateStaffById(String id, StaffUpdateRequest request, MultipartFile file) throws IOException {
        MedicalStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "auth-e-02"));

        staff.setAddress(request.getAddress());
        staff.setDob(request.getDob());
        staff.setPhone(request.getPhone());
        staff.setGender(request.isGender());
        staff.setStartDate(request.getStartDate());
        staff.setPracticingCertificate(request.getPracticingCertificate());
        staff.setFullName(request.getFullName());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lưu nhiều chuyên khoa
        Set<Specialties> specialties = new HashSet<>();
        for (String specialtyId : request.getSpecialtiesId()) { // Giả sử getSpecialtiesIds trả về danh sách ID chuyên khoa
            Specialties specialty = specialtiesRepository.findById(specialtyId)
                    .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Specialty not found: " + specialtyId, "specialty-not-found"));
            specialties.add(specialty);
        }
        staff.setSpecialties(specialties);
        HashSet<String> roles = new HashSet<>(request.getRoles());
        staff.setRoles(roles);

        HashSet<String> hocHam = new HashSet<>(request.getHocHam());
        staff.setHocHam(hocHam);

        HashSet<String> hocVi = new HashSet<>(request.getHocVi());
        staff.setHocVi(hocVi);
        if(!file.isEmpty()){
            String avatarUrl = uploadImg(file, staff.getEmail());
            staff.setAvatar(avatarUrl);
        }
        staff.setEnabled(request.isEnable());
        staff.setStatus(Status.valueOf(request.getStatus()));
        staffRepository.save(staff);
        return staffMapper.toStaffResponse(staff);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStaffById(String id){
        MedicalStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "auth-e-03"));
        staff.setEnabled(false);
        staffRepository.save(staff);
    }

}
