package org.telemedicine.server.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.request.*;
import org.telemedicine.server.dto.response.AuthResponse;
import org.telemedicine.server.dto.response.IntrospectResponse;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.service.AuthService;
import org.telemedicine.server.service.PatientService;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private AuthService authService;


    //Đăng ký cho user
    @PostMapping("/signup")
    ApiResponse<PatientResponse> signup(@RequestBody @Valid PatientCreationRequest request) {
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(authService.signUp(request));
        return apiResponse;
    }
    //đăng nhập cho user
    @PostMapping("/signinUser")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        var result = authService.authenticateUser(request);

        return ApiResponse.<AuthResponse>builder()
                .data(result)
                .build();
    }
    //đăng nhập cho nhân viên
    @PostMapping("/signinStaff")
    ApiResponse<AuthResponse> loginStaff(@RequestBody AuthRequest request) {
        var result = authService.authenticateStaff(request);

        return ApiResponse.<AuthResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("/refreshToken")
    ApiResponse<AuthResponse> introspect(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .build();
    }

}
