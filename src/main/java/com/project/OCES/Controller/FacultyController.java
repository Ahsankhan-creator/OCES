package com.project.OCES.Controller;

import com.project.OCES.Model.Faculty;
import com.project.OCES.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {
    
    @Autowired
    private DataService dataService;
    
    @GetMapping
    public ResponseEntity<Map<String, Faculty>> getAllFaculty() {
        return ResponseEntity.ok(dataService.getAllFaculties());
    }
    
    @GetMapping("/{facultyId}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable String facultyId) {
        return dataService.getFacultyById(facultyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        if (faculty == null || faculty.getFacultyId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dataService.addFaculty(faculty));
    }
}