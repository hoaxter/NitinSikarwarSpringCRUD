package com.pagati.studentapp.dao;

import com.pagati.studentapp.entity.Student;
import java.util.List;
import java.util.Optional;

// Data Access Object contract for the students table.
public interface StudentDao {

    Student insert(Student student);

    List<Student> fetchAll();

    Optional<Student> fetchById(int id);

    Student modify(int id, Student student);

    boolean remove(int id);
}
