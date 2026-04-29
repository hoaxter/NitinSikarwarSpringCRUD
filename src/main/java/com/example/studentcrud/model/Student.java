package com.example.studentcrud.model;

/**
 * Entity class representing a Student record.
 */
public class Student {

    private Integer id;
    private String name;
    private String email;
    private String course;

    // Default constructor
    public Student() {
    }

    // Parameterized constructor (without id, for creation)
    public Student(String name, String email, String course) {
        this.name = name;
        this.email = email;
        this.course = course;
    }

    // Parameterized constructor (with id, for retrieval)
    public Student(Integer id, String name, String email, String course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
