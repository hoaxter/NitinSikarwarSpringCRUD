package com.pagati.studentapp.entity;

// Plain Java object representing a row in the students table.
public class Student {

    private Integer id;
    private String name;
    private String email;
    private String course;

    public Student() { }

    public Student(String name, String email, String course) {
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public Student(Integer id, String name, String email, String course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public Integer getId()      { return id; }
    public String  getName()    { return name; }
    public String  getEmail()   { return email; }
    public String  getCourse()  { return course; }

    public void setId(Integer id)       { this.id = id; }
    public void setName(String name)    { this.name = name; }
    public void setEmail(String email)  { this.email = email; }
    public void setCourse(String course){ this.course = course; }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name +
               ", email=" + email + ", course=" + course + "]";
    }
}
