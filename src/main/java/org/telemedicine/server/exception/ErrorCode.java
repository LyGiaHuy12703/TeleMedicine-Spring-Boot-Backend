package org.telemedicine.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    PASSWORD_WRONG(1011, "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    STAFF_NOT_EXISTED(1007, "Nhân viên không được tìm thấy", HttpStatus.NOT_FOUND),
    STAFF_EXISTED(1008, "Nhân viên đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "Không tìm thấy tài khoản", HttpStatus.NOT_FOUND),
    UNCATEGORIZED(9999, "UNCATEGORIZED", HttpStatus.INTERNAL_SERVER_ERROR),//code 500
    FULL_NAME_INVALID(1002, "Họ tên phải ít nhất 5 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Mật khẩu phải ít nhất 6 ký tự", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    INVALID_EMAIL(1005, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHORIRED(1010, "You not permission", HttpStatus.FORBIDDEN),
    ;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = 200;
        this.message = message;
        this.statusCodes = httpStatus;
    }

    private int code;
    private String message;
    private HttpStatus statusCodes;
}
