package org.telemedicine.server.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.dto.request.*;
import org.telemedicine.server.dto.response.AuthResponse;
import org.telemedicine.server.dto.response.IntrospectResponse;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.service.AuthService;
import org.telemedicine.server.service.PatientService;

import java.net.URI;
import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private AuthService authService;


    //Đăng ký cho user
    @PostMapping("/signup")
    ApiResponse<PatientResponse> signup(@RequestBody @Valid PatientCreationRequest request) throws MessagingException {
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(authService.signUp(request));
        apiResponse.setMessage("Đăng ký tài khoản thành công, vui lòng xác nhận email");
        return apiResponse;
    }
    //xac nhan email user
    @GetMapping("/verify/{token}")
    ResponseEntity<Void> verifyUser(@PathVariable("token") String token) {
        boolean verified = authService.verifyUser(token);

        if (verified) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/auth/verification-success"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
//            return ApiResponse.builder()
//                    .code(200)
//                    .message("Xác nhận tài khoản thành công! Bạn có thể đăng nhập.")
//                    .build();
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/auth/verification-failed"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
//            return ApiResponse.builder()
//                    .code(200)
//                    .message("Token không hợp lệ hoặc đã hết hạn.")
//                    .build();
        }
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
    @PostMapping("/changePassword")
    ApiResponse<Object> changePassword(@RequestBody PasswordChangeRequest request) {
        authService.changePassword(request);
        return ApiResponse.builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .build();
    }

    @PostMapping("/forgotPassword")
    ApiResponse<Object> forgotPassword(@RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request);
        return ApiResponse.builder()
                .code(200)
                .message("Đổi mật khẩu thành công")
                .build();
    }

}
