package org.telemedicine.server.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telemedicine.server.core.ResponseSuccess;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.auth.*;
import org.telemedicine.server.dto.patients.PatientCreationRequest;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.service.AuthService;

import java.net.URI;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    //Đăng ký cho user
    @PostMapping("/register")
    ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid PatientCreationRequest request) throws MessagingException {
        authService.signUp(request);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-01")
                .message("Request register successfully, check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //xac nhan email user
    @GetMapping("/verify/{token}")
    ResponseEntity<Void> verifyUser(@PathVariable("token") String token) {
        boolean verified = authService.verifyUser(token);

        if (verified) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/auth/verification-success"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://localhost:3000/auth/verification-failed"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
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
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
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
