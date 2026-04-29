package com.pagati.studentapp.manager;

import com.pagati.studentapp.dao.StudentDao;
import com.pagati.studentapp.entity.Student;
import com.pagati.studentapp.error.StudentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// Business logic sits here; delegates persistence work to the DAO.
@Service
public class StudentManagerImpl implements StudentManager {

    private final StudentDao dao;

    public StudentManagerImpl(StudentDao dao) {
        this.dao = dao;
    }

    @Override
    public Student addStudent(Student student) {
        return dao.insert(student);
    }

    @Override
    public List<Student> listAll() {
        return dao.fetchAll();
    }

    @Override
    public Student findOne(int id) {
        return dao.fetchById(id)
                .orElseThrow(() ->
                    new StudentNotFoundException("No student exists with id " + id));
    }

    @Override
    public Student editStudent(int id, Student student) {
        // Make sure the record is present before updating
        dao.fetchById(id).orElseThrow(() ->
                new StudentNotFoundException("Cannot update — student with id " + id + " not found"));
        return dao.modify(id, student);
    }

    @Override
    public void removeStudent(int id) {
        boolean removed = dao.remove(id);
        if (!removed) {
            throw new StudentNotFoundException("Cannot delete — student with id " + id + " not found");
        }
    }
}
