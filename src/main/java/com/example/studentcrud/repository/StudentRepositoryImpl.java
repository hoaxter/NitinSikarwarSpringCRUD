package com.example.studentcrud.repository;

import com.example.studentcrud.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * JDBC-based implementation of {@link StudentRepository}.
 * Uses Spring's JdbcTemplate for executing SQL queries manually.
 */
@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * RowMapper to map each row of the ResultSet to a Student object.
     */
    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("course")
    );

    public StudentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Student save(Student student) {
        String sql = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            return ps;
        }, keyHolder);

        // Set the generated id back on the student object
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            student.setId(generatedId.intValue());
        }

        return student;
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT id, name, email, course FROM students ORDER BY id";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    @Override
    public Optional<Student> findById(Integer id) {
        String sql = "SELECT id, name, email, course FROM students WHERE id = ?";
        List<Student> results = jdbcTemplate.query(sql, studentRowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public Student update(Integer id, Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
        jdbcTemplate.update(sql, student.getName(), student.getEmail(), student.getCourse(), id);
        student.setId(id);
        return student;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM students WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
