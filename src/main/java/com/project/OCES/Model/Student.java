package com.project.OCES.Model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private List<String> enrolledCourses;
    private final int MAX_COURSE_LOAD = 5;
    
    public Student() {
        this.enrolledCourses = new ArrayList<>();
    }
    
    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrolledCourses = new ArrayList<>();
    }
    
    public boolean canEnrollInCourse(String courseCode) {
        return enrolledCourses.size() < MAX_COURSE_LOAD && 
               !enrolledCourses.contains(courseCode);
    }
    
    public boolean enrollInCourse(String courseCode) {
        if (canEnrollInCourse(courseCode)) {
            enrolledCourses.add(courseCode);
            return true;
        }
        return false;
    }
    
    public boolean dropCourse(String courseCode) {
        return enrolledCourses.remove(courseCode);
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<String> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }
    
    public int getCurrentCourseLoad() { return enrolledCourses.size(); }
    public int getMaxCourseLoad() { return MAX_COURSE_LOAD; }
}
