package com.project.OCES.Service;

import com.project.OCES.DTO.CourseInfo;
import com.project.OCES.DTO.EnrollmentResponse;
import com.project.OCES.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    
    @Autowired
    private DataService dataService;
    
    public EnrollmentResponse registerStudentForCourse(String studentId, String courseCode) {
        try {
            if (studentId == null || courseCode == null) {
                return new EnrollmentResponse(false, "Student ID and course code are required");
            }
            
            Optional<Student> studentOpt = dataService.getStudentById(studentId);
            Optional<Course> courseOpt = dataService.getCourseByCode(courseCode);
            
            if (studentOpt.isEmpty()) {
                return new EnrollmentResponse(false, "Student not found");
            }
            
            if (courseOpt.isEmpty()) {
                return new EnrollmentResponse(false, "Course not found");
            }
            
            Student student = studentOpt.get();
            Course course = courseOpt.get();
            
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
                Optional<Course> enrolledCourseOpt = dataService.getCourseByCode(enrolledCourseCode);
                if (enrolledCourseOpt.isPresent()) {
                    Course enrolledCourse = enrolledCourseOpt.get();
                    if (enrolledCourse.getScheduleSlot() != null && 
                        course.getScheduleSlot() != null &&
                        enrolledCourse.getScheduleSlot().conflictsWith(course.getScheduleSlot())) {
                        return new EnrollmentResponse(false, 
                            "Schedule conflict with " + enrolledCourse.getTitle() + 
                            " (" + enrolledCourse.getScheduleSlot() + ")");
                    }
                }
            }
            
            // Perform enrollment
            student.enrollInCourse(courseCode);
            course.addStudent(studentId);
            dataService.addEnrollment(new Enrollment(studentId, courseCode));
            
            // Send notification
            Notification notification = new Notification(
                studentId, 
                "You have successfully enrolled in " + course.getTitle()
            );
            dataService.addNotification(notification);
            
            return new EnrollmentResponse(true, "Successfully enrolled in " + course.getTitle());
            
        } catch (Exception e) {
            logger.error("Error enrolling student {} in course {}", studentId, courseCode, e);
            return new EnrollmentResponse(false, "Enrollment failed: " + e.getMessage());
        }
    }
    
    public EnrollmentResponse dropStudentFromCourse(String studentId, String courseCode) {
        try {
            if (studentId == null || courseCode == null) {
                return new EnrollmentResponse(false, "Student ID and course code are required");
            }
            
            Optional<Student> studentOpt = dataService.getStudentById(studentId);
            Optional<Course> courseOpt = dataService.getCourseByCode(courseCode);
            
            if (studentOpt.isEmpty()) {
                return new EnrollmentResponse(false, "Student not found");
            }
            
            if (courseOpt.isEmpty()) {
                return new EnrollmentResponse(false, "Course not found");
            }
            
            Student student = studentOpt.get();
            Course course = courseOpt.get();
            
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
            
            // Send notification
            Notification notification = new Notification(
                studentId, 
                "You have dropped from " + course.getTitle()
            );
            dataService.addNotification(notification);
            
            return new EnrollmentResponse(true, "Successfully dropped from " + course.getTitle());
            
        } catch (Exception e) {
            logger.error("Error dropping student {} from course {}", studentId, courseCode, e);
            return new EnrollmentResponse(false, "Drop failed: " + e.getMessage());
        }
    }
    
    public List<CourseInfo> getAvailableCourses() {
        Map<String, Course> allCourses = dataService.getAllCourses();
        if (allCourses == null || allCourses.isEmpty()) {
            return List.of();
        }

        return allCourses.values().stream()
                .filter(course -> course != null && 
                       (course.getStatus() == CourseStatus.ACTIVE || 
                        course.getStatus() == CourseStatus.UNDER_ENROLLED))
                .map(course -> {
                    Optional<Faculty> facultyOpt = dataService.getFacultyById(course.getFacultyId());
                    String facultyName = facultyOpt.map(Faculty::getName).orElse("Unknown");
                    return new CourseInfo(course, facultyName);
                })
                .collect(Collectors.toList());
    }
    
    public List<Student> getEnrolledStudentsInCourse(String courseCode) {
        if (courseCode == null) {
            return List.of();
        }
        
        Optional<Course> courseOpt = dataService.getCourseByCode(courseCode);
        if (courseOpt.isEmpty()) {
            return List.of();
        }
        
        return courseOpt.get().getEnrolledStudents().stream()
                .map(studentId -> dataService.getStudentById(studentId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    
    public List<CourseInfo> getStudentEnrolledCourses(String studentId) {
        if (studentId == null) {
            return List.of();
        }
        
        Optional<Student> studentOpt = dataService.getStudentById(studentId);
        if (studentOpt.isEmpty()) {
            return List.of();
        }
        
        return studentOpt.get().getEnrolledCourses().stream()
                .map(courseCode -> dataService.getCourseByCode(courseCode))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(course -> {
                    Optional<Faculty> facultyOpt = dataService.getFacultyById(course.getFacultyId());
                    String facultyName = facultyOpt.map(Faculty::getName).orElse("Unknown");
                    return new CourseInfo(course, facultyName);
                })
                .collect(Collectors.toList());
    }
    
    public EnrollmentResponse cancelCourse(String courseCode, String facultyId) {
        try {
            if (courseCode == null || facultyId == null) {
                return new EnrollmentResponse(false, "Course code and faculty ID are required");
            }
            
            Optional<Course> courseOpt = dataService.getCourseByCode(courseCode);
            if (courseOpt.isEmpty()) {
                return new EnrollmentResponse(false, "Course not found");
            }
            
            Course course = courseOpt.get();
            
            if (!course.getFacultyId().equals(facultyId)) {
                return new EnrollmentResponse(false, "You are not authorized to cancel this course");
            }
            
            // Drop all enrolled students
            List<String> enrolledStudents = new ArrayList<>(course.getEnrolledStudents());
            for (String studentId : enrolledStudents) {
                Optional<Student> studentOpt = dataService.getStudentById(studentId);
                if (studentOpt.isPresent()) {
                    Student student = studentOpt.get();
                    student.dropCourse(courseCode);
                    
                    // Send notification
                    Notification notification = new Notification(
                        studentId,
                        "Course " + course.getTitle() + " has been cancelled"
                    );
                    dataService.addNotification(notification);
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
            logger.error("Error cancelling course {}", courseCode, e);
            return new EnrollmentResponse(false, "Course cancellation failed: " + e.getMessage());
        }
    }
}