package org.telemedicine.server.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private boolean success = true;
    private String code;
    private String message;
    private T data;
}
