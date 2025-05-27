package com.project.OCES.Controller;

import com.project.OCES.DTO.EnrollmentRequest;
import com.project.OCES.DTO.CourseInfo;
import com.project.OCES.Model.Student;
import com.project.OCES.DTO.EnrollmentResponse;
import com.project.OCES.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentResponse> enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.registerStudentForCourse(
            request.getStudentId(), request.getCourseCode());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/drop")
    public ResponseEntity<EnrollmentResponse> dropCourse(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.dropStudentFromCourse(
            request.getStudentId(), request.getCourseCode());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/available-courses")
    public ResponseEntity<List<CourseInfo>> getAvailableCourses() {
        return ResponseEntity.ok(enrollmentService.getAvailableCourses());
    }
    
    @GetMapping("/student-courses/{studentId}")
    public ResponseEntity<List<CourseInfo>> getStudentCourses(@PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getStudentEnrolledCourses(studentId));
    }
    
    @GetMapping("/course-students/{courseCode}")
    public ResponseEntity<List<Student>> getEnrolledStudents(@PathVariable String courseCode) {
        return ResponseEntity.ok(enrollmentService.getEnrolledStudentsInCourse(courseCode));
    }
    
    @PostMapping("/cancel-course/{courseCode}")
    public ResponseEntity<EnrollmentResponse> cancelCourse(
            @PathVariable String courseCode, 
            @RequestParam String facultyId) {
        return ResponseEntity.ok(enrollmentService.cancelCourse(courseCode, facultyId));
    }
}