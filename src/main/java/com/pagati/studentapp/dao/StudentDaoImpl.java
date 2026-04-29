package com.pagati.studentapp.dao;

import com.pagati.studentapp.entity.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

// Concrete DAO that talks to PostgreSQL through JdbcTemplate.
@Repository
public class StudentDaoImpl implements StudentDao {

    private final JdbcTemplate jdbc;

    // Maps a single result-set row into a Student object.
    private static final RowMapper<Student> ROW_MAPPER = (resultSet, rowIndex) ->
            new Student(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("course")
            );

    public StudentDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Student insert(Student student) {
        final String query = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getCourse());
            return stmt;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            student.setId(key.intValue());
        }
        return student;
    }

    @Override
    public List<Student> fetchAll() {
        return jdbc.query("SELECT id, name, email, course FROM students ORDER BY id ASC", ROW_MAPPER);
    }

    @Override
    public Optional<Student> fetchById(int id) {
        List<Student> matches = jdbc.query(
                "SELECT id, name, email, course FROM students WHERE id = ?",
                ROW_MAPPER, id
        );
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
    }

    @Override
    public Student modify(int id, Student student) {
        jdbc.update(
            "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?",
            student.getName(), student.getEmail(), student.getCourse(), id
        );
        student.setId(id);
        return student;
    }

    @Override
    public boolean remove(int id) {
        int affected = jdbc.update("DELETE FROM students WHERE id = ?", id);
        return affected > 0;
    }
}
