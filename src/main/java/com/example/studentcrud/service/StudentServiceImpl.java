package com.example.studentcrud.service;

import com.example.studentcrud.exception.ResourceNotFoundException;
import com.example.studentcrud.model.Student;
import com.example.studentcrud.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link StudentService}.
 * Contains business logic and delegates data access to the repository layer.
 */
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    public Student updateStudent(Integer id, Student student) {
        // Verify the student exists before updating
        studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return studentRepository.update(id, student);
    }

    @Override
    public void deleteStudent(Integer id) {
        boolean deleted = studentRepository.deleteById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
    }
}
