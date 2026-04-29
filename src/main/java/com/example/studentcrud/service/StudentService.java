package com.example.studentcrud.service;

import com.example.studentcrud.model.Student;

import java.util.List;

/**
 * Service interface for Student business logic.
 */
public interface StudentService {

    Student createStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(Integer id);

    Student updateStudent(Integer id, Student student);

    void deleteStudent(Integer id);
}
