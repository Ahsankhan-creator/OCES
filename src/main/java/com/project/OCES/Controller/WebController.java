package com.project.OCES.Controller;

import com.project.OCES.DTO.CourseInfo;
import com.project.OCES.DTO.EnrollmentResponse;
import com.project.OCES.Model.Student;
import com.project.OCES.Service.DataService;
import com.project.OCES.Service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web")
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private DataService dataService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            model.addAttribute("students", dataService.getAllStudents().values());
            return "index";
        } catch (Exception e) {
            logger.error("Error loading home page", e);
            model.addAttribute("error", "Failed to load student data");
            return "error";
        }
    }
    
    @GetMapping("/courses")
    public String viewCourses(Model model) {
        try {
            List<CourseInfo> courses = enrollmentService.getAvailableCourses();
            model.addAttribute("courses", courses != null ? courses : List.of());
            return "courses";
        } catch (Exception e) {
            logger.error("Error loading courses page", e);
            model.addAttribute("error", "Failed to load course data");
            return "error";
        }
    }
    
    @GetMapping("/enroll/{studentId}")
    public String showEnrollmentForm(@PathVariable String studentId, Model model) {
        try {
            if (studentId == null || studentId.isBlank()) {
                return "redirect:/web/?error=Invalid+student+ID";
            }
            
            model.addAttribute("studentId", studentId);
            model.addAttribute("availableCourses", 
                enrollmentService.getAvailableCourses());
            model.addAttribute("enrolledCourses", 
                enrollmentService.getStudentEnrolledCourses(studentId));
            model.addAttribute("unreadCount", 
                dataService.getUnreadNotificationCount(studentId));
            return "enroll";
        } catch (Exception e) {
            logger.error("Error loading enrollment form for student {}", studentId, e);
            return "redirect:/web/?error=Failed+to+load+enrollment+data";
        }
    }
    
    @PostMapping("/enroll")
    public String enrollStudent(@RequestParam String studentId, 
                              @RequestParam String courseCode,
                              Model model) {
        try {
            if (studentId == null || courseCode == null) {
                return "redirect:/web/enroll/" + studentId + "?error=Invalid+request";
            }
            
            EnrollmentResponse response = enrollmentService.registerStudentForCourse(studentId, courseCode);
            model.addAttribute("message", response.getMessage());
            return "redirect:/web/enroll/" + studentId;
        } catch (Exception e) {
            logger.error("Error enrolling student {} in course {}", studentId, courseCode, e);
            return "redirect:/web/enroll/" + studentId + "?error=Enrollment+failed";
        }
    }
    
    @PostMapping("/drop")
    public String dropCourse(@RequestParam String studentId, 
                           @RequestParam String courseCode,
                           Model model) {
        try {
            if (studentId == null || courseCode == null) {
                return "redirect:/web/enroll/" + studentId + "?error=Invalid+request";
            }
            
            EnrollmentResponse response = enrollmentService.dropStudentFromCourse(studentId, courseCode);
            model.addAttribute("message", response.getMessage());
            return "redirect:/web/enroll/" + studentId;
        } catch (Exception e) {
            logger.error("Error dropping student {} from course {}", studentId, courseCode, e);
            return "redirect:/web/enroll/" + studentId + "?error=Drop+failed";
        }
    }
    
    @GetMapping("/course/{courseCode}")
    public String viewCourseDetails(@PathVariable String courseCode, Model model) {
        try {
            if (courseCode == null || courseCode.isBlank()) {
                return "redirect:/web/courses?error=Invalid+course+code";
            }
            
            List<CourseInfo> availableCourses = enrollmentService.getAvailableCourses();
            if (availableCourses == null) {
                return "redirect:/web/courses?error=No+courses+available";
            }
            
            CourseInfo course = availableCourses.stream()
                    .filter(c -> courseCode.equals(c.getCourseCode()))
                    .findFirst()
                    .orElse(null);
            
            if (course == null) {
                return "redirect:/web/courses?error=Course+not+found";
            }
            
            List<Student> students = enrollmentService.getEnrolledStudentsInCourse(courseCode);
            model.addAttribute("course", course);
            model.addAttribute("students", students != null ? students : List.of());
            return "course-details";
        } catch (Exception e) {
            logger.error("Error viewing course details for {}", courseCode, e);
            return "redirect:/web/courses?error=Failed+to+load+course+details";
        }
    }
    
    @GetMapping("/notifications/{userId}")
    public String viewNotifications(@PathVariable String userId, Model model) {
        try {
            if (userId == null || userId.isBlank()) {
                return "redirect:/web/?error=Invalid+user+ID";
            }
            
            model.addAttribute("notifications", dataService.getNotifications(userId));
            model.addAttribute("userId", userId);
            model.addAttribute("unreadCount", dataService.getUnreadNotificationCount(userId));
            return "notifications";
        } catch (Exception e) {
            logger.error("Error loading notifications for user {}", userId, e);
            return "redirect:/web/?error=Failed+to+load+notifications";
        }
    }
    
    @PostMapping("/notifications/mark-read/{userId}/{notificationId}")
    public String markNotificationAsRead(
            @PathVariable String userId,
            @PathVariable String notificationId) {
        try {
            if (userId == null || notificationId == null) {
                return "redirect:/web/notifications/" + userId + "?error=Invalid+request";
            }
            
            dataService.markAsRead(userId, notificationId);
            return "redirect:/web/notifications/" + userId;
        } catch (Exception e) {
            logger.error("Error marking notification {} as read for user {}", notificationId, userId, e);
            return "redirect:/web/notifications/" + userId + "?error=Failed+to+mark+as+read";
        }
    }
    
    @PostMapping("/cancel-course/{courseCode}")
    public String cancelCourse(@PathVariable String courseCode,
                             @RequestParam String facultyId,
                             Model model) {
        try {
            if (courseCode == null || facultyId == null) {
                return "redirect:/web/course/" + courseCode + "?error=Invalid+request";
            }
            
            EnrollmentResponse response = enrollmentService.cancelCourse(courseCode, facultyId);
            model.addAttribute("message", response.getMessage());
            return "redirect:/web/course/" + courseCode;
        } catch (Exception e) {
            logger.error("Error cancelling course {}", courseCode, e);
            return "redirect:/web/course/" + courseCode + "?error=Cancellation+failed";
        }
    }
}