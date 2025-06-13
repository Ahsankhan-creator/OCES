package com.project.OCES.Controller;

import com.project.OCES.Model.Student;
import com.project.OCES.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private DataService dataService;
    
    @GetMapping
    public ResponseEntity<Map<String, Student>> getAllStudents() {
        return ResponseEntity.ok(dataService.getAllStudents());
    }
    
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable String studentId) {
        return dataService.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        if (student == null || student.getStudentId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dataService.addStudent(student));
    }
}