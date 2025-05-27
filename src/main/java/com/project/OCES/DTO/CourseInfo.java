package com.project.OCES.DTO;

import com.project.OCES.Model.Course;
import com.project.OCES.Model.CourseStatus;
import com.project.OCES.Model.TimeSlot;

public class CourseInfo {
    private String courseCode;
    private String title;
    private int creditHours;
    private TimeSlot scheduleSlot;
    private int capacity;
    private int enrolledCount;
    private int availableSeats;
    private String facultyName;
    private CourseStatus status;
    
    public CourseInfo() {}
    
    public CourseInfo(Course course, String facultyName) {
        this.courseCode = course.getCourseCode();
        this.title = course.getTitle();
        this.creditHours = course.getCreditHours();
        this.scheduleSlot = course.getScheduleSlot();
        this.capacity = course.getCapacity();
        this.enrolledCount = course.getEnrolledCount();
        this.availableSeats = course.getAvailableSeats();
        this.facultyName = facultyName;
        this.status = course.getStatus();
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
    
    public int getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
    
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
}
