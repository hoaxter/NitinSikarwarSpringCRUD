package com.pagati.studentapp.web;

import com.pagati.studentapp.entity.Student;
import com.pagati.studentapp.manager.StudentManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Exposes REST endpoints for student CRUD.
@RestController
@RequestMapping("/students")
public class StudentRestController {

    private final StudentManager manager;

    public StudentRestController(StudentManager manager) {
        this.manager = manager;
    }

    // POST /students — register a new student
    @PostMapping
    public ResponseEntity<Student> register(@RequestBody Student student) {
        Student saved = manager.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /students — list every student
    @GetMapping
    public ResponseEntity<List<Student>> listAll() {
        return ResponseEntity.ok(manager.listAll());
    }

    // GET /students/{id} — look up one student
    @GetMapping("/{id}")
    public ResponseEntity<Student> lookup(@PathVariable int id) {
        return ResponseEntity.ok(manager.findOne(id));
    }

    // PUT /students/{id} — edit an existing student
    @PutMapping("/{id}")
    public ResponseEntity<Student> edit(@PathVariable int id, @RequestBody Student student) {
        return ResponseEntity.ok(manager.editStudent(id, student));
    }

    // DELETE /students/{id} — remove a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable int id) {
        manager.removeStudent(id);
        return ResponseEntity.noContent().build();
    }
}
