package com.project.OCES.Service;

import com.project.OCES.Model.*;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataService {
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final Map<String, Faculty> faculties = new ConcurrentHashMap<>();
    private final List<Enrollment> enrollments = Collections.synchronizedList(new ArrayList<>());
    
    @PostConstruct
    public void initializeData() {
        // Initialize Faculty
        Faculty faculty1 = new Faculty("F001", "Dr. John Smith", "john.smith@university.edu");
        Faculty faculty2 = new Faculty("F002", "Dr. Sarah Johnson", "sarah.johnson@university.edu");
        Faculty faculty3 = new Faculty("F003", "Dr. Michael Brown", "michael.brown@university.edu");
        Faculty faculty4 = new Faculty("F004", "Dr. Emily Davis", "emily.davis@university.edu");
        
        faculties.put("F001", faculty1);
        faculties.put("F002", faculty2);
        faculties.put("F003", faculty3);
        faculties.put("F004", faculty4);
        
        // Initialize Courses
        Course course1 = new Course("CS101", "Introduction to Computer Science", 3,
                new TimeSlot("Monday", LocalTime.of(9, 0), LocalTime.of(10, 30)), 30, "F001");
        Course course2 = new Course("CS102", "Data Structures", 4,
                new TimeSlot("Tuesday", LocalTime.of(11, 0), LocalTime.of(12, 30)), 25, "F001");
        Course course3 = new Course("MATH201", "Calculus I", 4,
                new TimeSlot("Monday", LocalTime.of(14, 0), LocalTime.of(15, 30)), 35, "F002");
        Course course4 = new Course("PHYS101", "Physics I", 3,
                new TimeSlot("Wednesday", LocalTime.of(9, 0), LocalTime.of(10, 30)), 30, "F003");
        Course course5 = new Course("ENG101", "English Composition", 3,
                new TimeSlot("Thursday", LocalTime.of(13, 0), LocalTime.of(14, 30)), 20, "F004");
        Course course6 = new Course("CS201", "Algorithms", 4,
                new TimeSlot("Friday", LocalTime.of(10, 0), LocalTime.of(11, 30)), 25, "F001");
        
        courses.put("CS101", course1);
        courses.put("CS102", course2);
        courses.put("MATH201", course3);
        courses.put("PHYS101", course4);
        courses.put("ENG101", course5);
        courses.put("CS201", course6);
        
        // Initialize Students
        Student student1 = new Student("S001", "Alice Johnson", "alice.johnson@student.edu");
        Student student2 = new Student("S002", "Bob Smith", "bob.smith@student.edu");
        Student student3 = new Student("S003", "Carol Williams", "carol.williams@student.edu");
        Student student4 = new Student("S004", "David Brown", "david.brown@student.edu");
        Student student5 = new Student("S005", "Eva Davis", "eva.davis@student.edu");
        
        students.put("S001", student1);
        students.put("S002", student2);
        students.put("S003", student3);
        students.put("S004", student4);
        students.put("S005", student5);
    }
    
    // Student operations
    public Map<String, Student> getAllStudents() { return new HashMap<>(students); }
    public Student getStudentById(String studentId) { return students.get(studentId); }
    public Student addStudent(Student student) { return students.put(student.getStudentId(), student); }
    
    // Course operations
    public Map<String, Course> getAllCourses() { return new HashMap<>(courses); }
    public Course getCourseByCode(String courseCode) { return courses.get(courseCode); }
    public Course addCourse(Course course) { return courses.put(course.getCourseCode(), course); }
    
    // Faculty operations
    public Map<String, Faculty> getAllFaculties() { return new HashMap<>(faculties); }
    public Faculty getFacultyById(String facultyId) { return faculties.get(facultyId); }
    public Faculty addFaculty(Faculty faculty) { return faculties.put(faculty.getFacultyId(), faculty); }
    
    // Enrollment operations
    public List<Enrollment> getAllEnrollments() { return new ArrayList<>(enrollments); }
    public void addEnrollment(Enrollment enrollment) { enrollments.add(enrollment); }
    
    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
    
    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getCourseCode().equals(courseCode))
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
}