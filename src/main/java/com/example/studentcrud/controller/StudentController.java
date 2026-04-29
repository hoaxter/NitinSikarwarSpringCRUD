package com.example.studentcrud.controller;

import com.example.studentcrud.model.Student;
import com.example.studentcrud.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing CRUD endpoints for Student resources.
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * POST /students → Create a new student.
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student created = studentService.createStudent(student);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * GET /students → Retrieve all students.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * GET /students/{id} → Retrieve a student by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * PUT /students/{id} → Update a student by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        Student updated = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /students/{id} → Delete a student by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
