package com.nitin.enrollhub.support;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Intercepts exceptions globally and returns structured JSON responses.
 *
 * @author Nitin
 */
@RestControllerAdvice
public class FailureInterceptor {

    /**
     * Lightweight record-like structure for error payloads.
     */
    static class ApiError {
        private final String timestamp;
        private final int status;
        private final String reason;
        private final String message;

        ApiError(int status, String reason, String message) {
            this.timestamp = LocalDateTime.now().toString();
            this.status = status;
            this.reason = reason;
            this.message = message;
        }

        public String getTimestamp() { return timestamp; }
        public int getStatus()      { return status; }
        public String getReason()   { return reason; }
        public String getMessage()  { return message; }
    }

    @ExceptionHandler(NoSuchEnrolleeException.class)
    public ResponseEntity<ApiError> onNotFound(NoSuchEnrolleeException ex) {
        ApiError body = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Enrollee Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> onUnexpected(Exception ex) {
        ApiError body = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Failure",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
