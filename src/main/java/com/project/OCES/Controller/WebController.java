package com.project.OCES.Controller;

import com.project.OCES.DTO.CourseInfo;
import com.project.OCES.DTO.EnrollmentResponse;
import com.project.OCES.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web")
public class WebController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/courses")
    public String viewCourses(Model model) {
        model.addAttribute("courses", enrollmentService.getAvailableCourses());
        return "courses";
    }
    
    @GetMapping("/enroll/{studentId}")
    public String showEnrollmentForm(@PathVariable String studentId, Model model) {
        model.addAttribute("studentId", studentId);
        model.addAttribute("availableCourses", enrollmentService.getAvailableCourses());
        model.addAttribute("enrolledCourses", enrollmentService.getStudentEnrolledCourses(studentId));
        return "enroll";
    }
    
    @PostMapping("/enroll")
    public String enrollStudent(@RequestParam String studentId, 
                              @RequestParam String courseCode,
                              Model model) {
        EnrollmentResponse response = enrollmentService.registerStudentForCourse(studentId, courseCode);
        model.addAttribute("message", response.getMessage());
        return "redirect:/web/enroll/" + studentId;
    }
    
    @PostMapping("/drop")
    public String dropCourse(@RequestParam String studentId, 
                           @RequestParam String courseCode,
                           Model model) {
        EnrollmentResponse response = enrollmentService.dropStudentFromCourse(studentId, courseCode);
        model.addAttribute("message", response.getMessage());
        return "redirect:/web/enroll/" + studentId;
    }
    
    @GetMapping("/course/{courseCode}")
    public String viewCourseDetails(@PathVariable String courseCode, Model model) {
        CourseInfo course = enrollmentService.getAvailableCourses().stream()
                .filter(c -> c.getCourseCode().equals(courseCode))
                .findFirst()
                .orElse(null);
        
        if (course == null) {
            return "redirect:/web/courses";
        }
        
        model.addAttribute("course", course);
        model.addAttribute("students", enrollmentService.getEnrolledStudentsInCourse(courseCode));
        return "course-details";
    }
}

