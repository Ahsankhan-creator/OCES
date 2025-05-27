package com.project.OCES.DTO;

import jakarta.validation.constraints.NotBlank;

public class EnrollmentRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "Course code is required")
    private String courseCode;
    
    public EnrollmentRequest() {}
    
    public EnrollmentRequest(String studentId, String courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
    }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
}

