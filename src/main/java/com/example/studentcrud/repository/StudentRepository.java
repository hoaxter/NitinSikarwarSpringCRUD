package com.example.studentcrud.repository;

import com.example.studentcrud.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface defining CRUD operations for Student entity.
 */
public interface StudentRepository {

    /**
     * Insert a new student record into the database.
     *
     * @param student the student to create
     * @return the created student with generated id
     */
    Student save(Student student);

    /**
     * Retrieve all student records.
     *
     * @return list of all students
     */
    List<Student> findAll();

    /**
     * Retrieve a student by their id.
     *
     * @param id the student id
     * @return an Optional containing the student if found, or empty
     */
    Optional<Student> findById(Integer id);

    /**
     * Update an existing student record.
     *
     * @param id      the id of the student to update
     * @param student the updated student data
     * @return the updated student
     */
    Student update(Integer id, Student student);

    /**
     * Delete a student record by id.
     *
     * @param id the student id
     * @return true if a record was deleted, false otherwise
     */
    boolean deleteById(Integer id);
}
