package org.telemedicine.server.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import org.telemedicine.server.core.ResponseSuccess;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.auth.*;
import org.telemedicine.server.dto.patients.PatientCreationRequest;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.enums.Role;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.service.AuthService;
import org.telemedicine.server.service.MailService;
import org.telemedicine.server.utils.CodeUtil;

import java.net.URI;
import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CodeUtil codeUtil;
    @Autowired
    private MailService mailService;
    @Autowired
    private MedicalStaffRepository medicalStaffRepository;

    @GetMapping("/test")
    String test() {
        MedicalStaff staff = medicalStaffRepository.findByEmail("admin@email.com")
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "not found"));
        return staff.getRoles().toString();
    }

    //Đăng ký cho user
    @PostMapping("/register")
    ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid PatientCreationRequest request) throws MessagingException {
        authService.register(request);
        String verificationCode = UUID.randomUUID().toString();
        codeUtil.save(verificationCode, request, 1);
        mailService.sendEmailToVerifyRegister(request.getEmail(), verificationCode);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-01")
                .message("Request register successfully, check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //xac nhan email user
    @GetMapping("/register/verify/{verificationCode}")
    public RedirectView verifyRegister(@PathVariable String verificationCode) {
        PatientCreationRequest request = (PatientCreationRequest) codeUtil.get(verificationCode);
        AuthResponse authResponse = authService.verifyRegister(request);
        codeUtil.remove(verificationCode);
        mailService.sendEmailToWelcome(request.getEmail());
        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/verification-success")
                .queryParam("accessToken",  authResponse.getAccessToken())
                .queryParam("refreshToken", authResponse.getRefreshToken())
                .toUriString();
        return new RedirectView(redirectUrl);
    }
    //đăng nhập cho user
    @PostMapping("/signInUser")
    ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        var result = authService.loginUser(request);

        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .data(result)
                .code("auth-s-02")
                .message("Login successful")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //đăng nhập cho nhân viên
    @PostMapping("/signInStaff")
    ResponseEntity<ApiResponse<AuthResponse>> loginStaff(@RequestBody AuthRequest request) {
        var result = authService.loginStaff(request);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .data(result)
                .code("auth-s-02")
                .message("Login successful")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PostMapping("/refreshToken")
    public ResponseSuccess refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        AuthResponse result = authService.refreshToken(request);
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Refresh Token Success")
                .metadata(result)
                .build();
    }


    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-03")
                .message("Logout successful")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/changePassword")
    ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody PasswordChangeRequest request) {
        authService.changePassword(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-04")
                .message("Change password successful")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/forgotPassword")
    ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-04")
                .message("Change password successful")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
