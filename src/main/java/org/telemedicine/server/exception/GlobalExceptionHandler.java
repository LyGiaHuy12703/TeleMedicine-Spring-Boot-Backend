package org.telemedicine.server.exception;

import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telemedicine.server.dto.api.ApiResponse;
import org.telemedicine.server.dto.api.ValidationError;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException e) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(e.getMessage())
                .code(e.getCode())
                .build();
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .errors(errors)
                .code("global-e-01")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    ResponseEntity<ApiResponse<Void>> handlingAuthenticationException(AuthenticationException e) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code("auth-e-01")
                .message("Authentication failed: " + e.getMessage())
                .build();
        return ResponseEntity.status(401).body(response); // 401 Unauthorized
    }

    @ExceptionHandler(value = java.nio.file.AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>> handlingAccessDeniedException(AccessDeniedException e) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code("auth-e-02")
                .message("Không có quyền truy cập: " + e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // 403 Forbidden
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingException(Exception e) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .code("global-e-01")
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}