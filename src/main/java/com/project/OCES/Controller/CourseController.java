package com.project.OCES.Controller;

import com.project.OCES.Model.Course;
import com.project.OCES.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private DataService dataService;
    
    @GetMapping
    public ResponseEntity<Map<String, Course>> getAllCourses() {
        return ResponseEntity.ok(dataService.getAllCourses());
    }
    
    @GetMapping("/{courseCode}")
    public ResponseEntity<Course> getCourse(@PathVariable String courseCode) {
        return dataService.getCourseByCode(courseCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        if (course == null || course.getCourseCode() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dataService.addCourse(course));
    }
}