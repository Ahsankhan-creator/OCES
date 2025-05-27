package com.project.OCES.Model;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private String facultyId;
    private String name;
    private String email;
    private List<String> assignedCourses;
    
    public Faculty() {
        this.assignedCourses = new ArrayList<>();
    }
    
    public Faculty(String facultyId, String name, String email) {
        this.facultyId = facultyId;
        this.name = name;
        this.email = email;
        this.assignedCourses = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<String> getAssignedCourses() { return assignedCourses; }
    public void setAssignedCourses(List<String> assignedCourses) { this.assignedCourses = assignedCourses; }
}