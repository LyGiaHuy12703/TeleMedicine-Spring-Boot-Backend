package org.telemedicine.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    STAFF_NOT_EXISTED(1007, "Staff not existed", HttpStatus.NOT_FOUND),
    STAFF_EXISTED(1008, "Staff already existed", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "USER ALREADY EXISTS", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "USER NOT EXISTS", HttpStatus.NOT_FOUND),
    UNCATEGORIZED(9999, "UNCATEGORIZED", HttpStatus.INTERNAL_SERVER_ERROR),//code 500
    FULL_NAME_INVALID(1002, "FULL NAME MUST BE LEAST 5 CHARACTERS", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "PASSWORD MUST BE LEAST 6 CHARACTERS", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    INVALID_EMAIL(1005, "INVALID EMAIL FORMAT", HttpStatus.BAD_REQUEST),
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
