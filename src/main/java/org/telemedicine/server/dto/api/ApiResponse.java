package org.telemedicine.server.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private boolean success = true;
    private String code;
    private String message;
    private T data;
    private List<ValidationError> errors;  // Thêm trường lỗi
    // Phương thức tiện ích để tạo phản hồi lỗi
    public static <T> ApiResponse<T> error(String code, List<ValidationError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .errors(errors)
                .build();
    }
}
