package com.pagati.studentapp.error;

// Thrown when a requested student record does not exist.
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String msg) {
        super(msg);
    }
}
