package com.pagati.studentapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// Centralised handler that converts exceptions into JSON error responses.
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(StudentNotFoundException ex) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("time", Instant.now().toString());
        payload.put("code", 404);
        payload.put("error", "Not Found");
        payload.put("detail", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payload);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(Exception ex) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("time", Instant.now().toString());
        payload.put("code", 500);
        payload.put("error", "Server Error");
        payload.put("detail", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload);
    }
}
