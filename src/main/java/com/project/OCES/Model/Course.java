package com.project.OCES.Model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseCode;
    private String title;
    private int creditHours;
    private TimeSlot scheduleSlot;
    private int capacity;
    private List<String> enrolledStudents;
    private String facultyId;
    private CourseStatus status;
    
    public Course() {
        this.enrolledStudents = new ArrayList<>();
        this.status = CourseStatus.ACTIVE;
    }
    
    public Course(String courseCode, String title, int creditHours, 
                  TimeSlot scheduleSlot, int capacity, String facultyId) {
        this.courseCode = courseCode;
        this.title = title;
        this.creditHours = creditHours;
        this.scheduleSlot = scheduleSlot;
        this.capacity = capacity;
        this.facultyId = facultyId;
        this.enrolledStudents = new ArrayList<>();
        this.status = CourseStatus.ACTIVE;
    }
    
    public boolean addStudent(String studentId) {
        if (enrolledStudents.size() >= capacity) {
            return false;
        }
        if (!enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            updateStatus();
            return true;
        }
        return false;
    }
    
    public boolean removeStudent(String studentId) {
        boolean removed = enrolledStudents.remove(studentId);
        if (removed) {
            updateStatus();
        }
        return removed;
    }
    
    private void updateStatus() {
        if (enrolledStudents.size() < 5 && status == CourseStatus.ACTIVE) {
            status = CourseStatus.UNDER_ENROLLED;
        } else if (enrolledStudents.size() >= 5 && status == CourseStatus.UNDER_ENROLLED) {
            status = CourseStatus.ACTIVE;
        }
    }
    
    public boolean isAtCapacity() {
        return enrolledStudents.size() >= capacity;
    }
    
    public int getAvailableSeats() {
        return capacity - enrolledStudents.size();
    }
    
    public boolean hasMinimumEnrollment() {
        return enrolledStudents.size() >= 5;
    }
    
    // Getters and Setters
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }
    
    public TimeSlot getScheduleSlot() { return scheduleSlot; }
    public void setScheduleSlot(TimeSlot scheduleSlot) { this.scheduleSlot = scheduleSlot; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public List<String> getEnrolledStudents() { return new ArrayList<>(enrolledStudents); }
    public void setEnrolledStudents(List<String> enrolledStudents) { this.enrolledStudents = enrolledStudents; }
    
    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
    
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    
    public int getEnrolledCount() { return enrolledStudents.size(); }
}