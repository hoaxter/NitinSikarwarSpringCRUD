package com.pagati.studentapp.manager;

import com.pagati.studentapp.entity.Student;
import java.util.List;

// Business-layer contract for student operations.
public interface StudentManager {

    Student addStudent(Student student);

    List<Student> listAll();

    Student findOne(int id);

    Student editStudent(int id, Student student);

    void removeStudent(int id);
}
