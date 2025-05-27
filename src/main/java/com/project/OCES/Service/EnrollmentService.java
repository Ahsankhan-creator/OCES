package com.project.OCES.Service;

import com.project.OCES.DTO.CourseInfo;
import com.project.OCES.DTO.EnrollmentResponse;
import com.project.OCES.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    
    @Autowired
    private DataService dataService;
    
    public EnrollmentResponse registerStudentForCourse(String studentId, String courseCode) {
        try {
            Student student = dataService.getStudentById(studentId);
            Course course = dataService.getCourseByCode(courseCode);
            
            if (student == null) {
                return new EnrollmentResponse(false, "Student not found");
            }
            
            if (course == null) {
                return new EnrollmentResponse(false, "Course not found");
            }
            
            if (course.getStatus() == CourseStatus.CANCELLED) {
                return new EnrollmentResponse(false, "Course has been cancelled");
            }
            
            if (!student.canEnrollInCourse(courseCode)) {
                if (student.getCurrentCourseLoad() >= student.getMaxCourseLoad()) {
                    return new EnrollmentResponse(false, "Maximum course load exceeded (5 courses)");
                }
                if (student.getEnrolledCourses().contains(courseCode)) {
                    return new EnrollmentResponse(false, "Already enrolled in this course");
                }
            }
            
            if (course.isAtCapacity()) {
                return new EnrollmentResponse(false, "Course is at full capacity");
            }
            
            // Check for schedule conflicts
            List<String> studentCourses = student.getEnrolledCourses();
            for (String enrolledCourseCode : studentCourses) {
                Course enrolledCourse = dataService.getCourseByCode(enrolledCourseCode);
                if (enrolledCourse != null && 
                    enrolledCourse.getScheduleSlot().conflictsWith(course.getScheduleSlot())) {
                    return new EnrollmentResponse(false, 
                        "Schedule conflict with " + enrolledCourse.getTitle() + 
                        " (" + enrolledCourse.getScheduleSlot() + ")");
                }
            }
            
            // Perform enrollment
            student.enrollInCourse(courseCode);
            course.addStudent(studentId);
            dataService.addEnrollment(new Enrollment(studentId, courseCode));
            
            return new EnrollmentResponse(true, "Successfully enrolled in " + course.getTitle());
            
        } catch (Exception e) {
            return new EnrollmentResponse(false, "Enrollment failed: " + e.getMessage());
        }
    }
    
    public EnrollmentResponse dropStudentFromCourse(String studentId, String courseCode) {
        try {
            Student student = dataService.getStudentById(studentId);
            Course course = dataService.getCourseByCode(courseCode);
            
            if (student == null) {
                return new EnrollmentResponse(false, "Student not found");
            }
            
            if (course == null) {
                return new EnrollmentResponse(false, "Course not found");
            }
            
            if (!student.getEnrolledCourses().contains(courseCode)) {
                return new EnrollmentResponse(false, "Student is not enrolled in this course");
            }
            
            // Perform drop
            student.dropCourse(courseCode);
            course.removeStudent(studentId);
            
            // Update enrollment status
            List<Enrollment> enrollments = dataService.getAllEnrollments();
            enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId) && 
                            e.getCourseCode().equals(courseCode) &&
                            e.getStatus() == EnrollmentStatus.ENROLLED)
                .findFirst()
                .ifPresent(e -> e.setStatus(EnrollmentStatus.DROPPED));
            
            return new EnrollmentResponse(true, "Successfully dropped from " + course.getTitle());
            
        } catch (Exception e) {
            return new EnrollmentResponse(false, "Drop failed: " + e.getMessage());
        }
    }
    
    public List<CourseInfo> getAvailableCourses() {
        return dataService.getAllCourses().values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE || 
                                course.getStatus() == CourseStatus.UNDER_ENROLLED)
                .map(course -> {
                    Faculty faculty = dataService.getFacultyById(course.getFacultyId());
                    String facultyName = faculty != null ? faculty.getName() : "Unknown";
                    return new CourseInfo(course, facultyName);
                })
                .collect(Collectors.toList());
    }
    
    public List<Student> getEnrolledStudentsInCourse(String courseCode) {
        Course course = dataService.getCourseByCode(courseCode);
        if (course == null) {
            return List.of();
        }
        
        return course.getEnrolledStudents().stream()
                .map(studentId -> dataService.getStudentById(studentId))
                .filter(student -> student != null)
                .collect(Collectors.toList());
    }
    
    public List<CourseInfo> getStudentEnrolledCourses(String studentId) {
        Student student = dataService.getStudentById(studentId);
        if (student == null) {
            return List.of();
        }
        
        return student.getEnrolledCourses().stream()
                .map(courseCode -> dataService.getCourseByCode(courseCode))
                .filter(course -> course != null)
                .map(course -> {
                    Faculty faculty = dataService.getFacultyById(course.getFacultyId());
                    String facultyName = faculty != null ? faculty.getName() : "Unknown";
                    return new CourseInfo(course, facultyName);
                })
                .collect(Collectors.toList());
    }
    
    public EnrollmentResponse cancelCourse(String courseCode, String facultyId) {
        try {
            Course course = dataService.getCourseByCode(courseCode);
            if (course == null) {
                return new EnrollmentResponse(false, "Course not found");
            }
            
            if (!course.getFacultyId().equals(facultyId)) {
                return new EnrollmentResponse(false, "You are not authorized to cancel this course");
            }
            
            // Drop all enrolled students
            List<String> enrolledStudents = course.getEnrolledStudents();
            for (String studentId : enrolledStudents) {
                Student student = dataService.getStudentById(studentId);
                if (student != null) {
                    student.dropCourse(courseCode);
                }
            }
            
            // Update course status
            course.setStatus(CourseStatus.CANCELLED);
            course.getEnrolledStudents().clear();
            
            // Update enrollments
            List<Enrollment> enrollments = dataService.getAllEnrollments();
            enrollments.stream()
                .filter(e -> e.getCourseCode().equals(courseCode) && 
                            e.getStatus() == EnrollmentStatus.ENROLLED)
                            .forEach(e -> e.setStatus(EnrollmentStatus.DROPPED));
            
            return new EnrollmentResponse(true, "Course cancelled successfully");
            
        } catch (Exception e) {
            return new EnrollmentResponse(false, "Course cancellation failed: " + e.getMessage());
        }
    }
}