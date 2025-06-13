package com.project.OCES.Service;

import com.project.OCES.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);
    
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final Map<String, Faculty> faculties = new ConcurrentHashMap<>();
    private final List<Enrollment> enrollments = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, List<Notification>> notifications = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initializeData() {
        try {
            // Initialize Faculty
            Faculty faculty1 = new Faculty("F001", "Dr. Syed Sameed Abbas", "syed.sameed@university.edu");
            Faculty faculty2 = new Faculty("F002", "Dr. Ali Raza", "ali.raza@university.edu");
            Faculty faculty3 = new Faculty("F003", "Dr. Zaid Khan", "zaid.khan@university.edu");
            Faculty faculty4 = new Faculty("F004", "Dr. Abdullah", "abdullah@university.edu");
            
            faculties.put("F001", faculty1);
            faculties.put("F002", faculty2);
            faculties.put("F003", faculty3);
            faculties.put("F004", faculty4);
            
            // Initialize Courses
            Course course1 = new Course("SDA101", "Software Design and Architecture", 3,
                    new TimeSlot("Monday", LocalTime.of(9, 0), LocalTime.of(10, 30)), 30, "F001");
            Course course2 = new Course("SQA102", "Software Quality Assurance", 4,
                    new TimeSlot("Tuesday", LocalTime.of(11, 0), LocalTime.of(12, 30)), 25, "F001");
            Course course3 = new Course("DCCN201", "Data Communication and Computer Networks", 4,
                    new TimeSlot("Monday", LocalTime.of(14, 0), LocalTime.of(15, 30)), 35, "F002");
            Course course4 = new Course("LAB101", "Data Communication and Computer Networks Lab", 3,
                    new TimeSlot("Wednesday", LocalTime.of(9, 0), LocalTime.of(10, 30)), 30, "F003");
            Course course5 = new Course("ES101", "Embedded System", 3,
                    new TimeSlot("Thursday", LocalTime.of(13, 0), LocalTime.of(14, 30)), 20, "F004");
            Course course6 = new Course("CV201", "Complex Variable", 4,
                    new TimeSlot("Friday", LocalTime.of(10, 0), LocalTime.of(11, 30)), 25, "F001");
            
            courses.put("SDA101", course1);
            courses.put("SQA102", course2);
            courses.put("DCCN201", course3);
            courses.put("LAB101", course4);
            courses.put("ES101", course5);
            courses.put("CV201", course6);
            
            // Initialize Students
            Student student1 = new Student("S001", "Muhammad Ahsan Khan", "muhammad.ahsan@student.edu");
            Student student2 = new Student("S002", "Zaid Khan", "zaid.khan@student.edu");
            Student student3 = new Student("S003", "Abdullah Khan", "abdullah.khan@student.edu");
            Student student4 = new Student("S004", "Ali Raza", "ali.raza@student.edu");
            Student student5 = new Student("S005", "Malik", "malik@student.edu");
            Student student6 = new Student("S006", "Adil Wagan", "adil.wagan@student.edu");
            Student student7 = new Student("S007", "Mujeed Bhoto", "mujeed.bhoto@student.edu");
            Student student8 = new Student("S008", "Ayesha Khan", "ayesha.khan@student.edu");
            Student student9 = new Student("S009", "Abdul Hadi", "abdul.hadi@student.edu");
            Student student10 = new Student("S010", "Muhammad Rizwan", "muhammad.rizwan@student.edu");
            
            students.put("S001", student1);
            students.put("S002", student2);
            students.put("S003", student3);
            students.put("S004", student4);
            students.put("S005", student5);
            students.put("S006", student6);
            students.put("S007", student7);
            students.put("S008", student8);
            students.put("S009", student9);
            students.put("S010", student10);
            
            logger.info("Initialized {} students, {} courses, {} faculty", 
                students.size(), courses.size(), faculties.size());
        } catch (Exception e) {
            logger.error("Error initializing test data", e);
            throw new IllegalStateException("Failed to initialize test data", e);
        }
    }
    
    // Student operations
    public Map<String, Student> getAllStudents() { 
        return new HashMap<>(students); 
    }
    
    public Optional<Student> getStudentById(String studentId) { 
        if (studentId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(students.get(studentId)); 
    }
    
    public Student addStudent(Student student) { 
        if (student == null || student.getStudentId() == null) {
            throw new IllegalArgumentException("Student and student ID cannot be null");
        }
        return students.put(student.getStudentId(), student); 
    }
    
    // Course operations
    public Map<String, Course> getAllCourses() { 
        return new HashMap<>(courses); 
    }
    
    public Optional<Course> getCourseByCode(String courseCode) { 
        if (courseCode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(courses.get(courseCode)); 
    }
    
    public Course addCourse(Course course) { 
        if (course == null || course.getCourseCode() == null) {
            throw new IllegalArgumentException("Course and course code cannot be null");
        }
        return courses.put(course.getCourseCode(), course); 
    }
    
    // Faculty operations
    public Map<String, Faculty> getAllFaculties() { 
        return new HashMap<>(faculties); 
    }
    
    public Optional<Faculty> getFacultyById(String facultyId) { 
        if (facultyId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(faculties.get(facultyId)); 
    }
    
    public Faculty addFaculty(Faculty faculty) { 
        if (faculty == null || faculty.getFacultyId() == null) {
            throw new IllegalArgumentException("Faculty and faculty ID cannot be null");
        }
        return faculties.put(faculty.getFacultyId(), faculty); 
    }
    
    // Enrollment operations
    public List<Enrollment> getAllEnrollments() { 
        synchronized(enrollments) {
            return new ArrayList<>(enrollments); 
        }
    }
    
    public void addEnrollment(Enrollment enrollment) { 
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }
        synchronized(enrollments) {
            enrollments.add(enrollment); 
        }
    }
    
    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        if (studentId == null) {
            return List.of();
        }
        synchronized(enrollments) {
            return enrollments.stream()
                    .filter(e -> studentId.equals(e.getStudentId()))
                    .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                    .collect(Collectors.toList());
        }
    }
    
    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        if (courseCode == null) {
            return List.of();
        }
        synchronized(enrollments) {
            return enrollments.stream()
                    .filter(e -> courseCode.equals(e.getCourseCode()))
                    .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                    .collect(Collectors.toList());
        }
    }
    
    // Notification operations
    public void addNotification(Notification notification) {
        if (notification == null || notification.getRecipientId() == null) {
            throw new IllegalArgumentException("Notification and recipient ID cannot be null");
        }
        notifications.computeIfAbsent(notification.getRecipientId(), k -> 
            Collections.synchronizedList(new ArrayList<>())).add(notification);
    }
    
    public List<Notification> getNotifications(String recipientId) {
        if (recipientId == null) {
            return List.of();
        }
        return notifications.getOrDefault(recipientId, Collections.synchronizedList(new ArrayList<>()));
    }
    
    public synchronized void markAsRead(String recipientId, String notificationId) {
        if (recipientId == null || notificationId == null) {
            throw new IllegalArgumentException("Recipient ID and notification ID cannot be null");
        }
        List<Notification> userNotifications = notifications.get(recipientId);
        if (userNotifications != null) {
            userNotifications.stream()
                .filter(n -> notificationId.equals(n.getId()))
                .findFirst()
                .ifPresent(n -> n.setRead(true));
        }
    }
    
    public int getUnreadNotificationCount(String recipientId) {
        if (recipientId == null) {
            return 0;
        }
        List<Notification> userNotifications = notifications.get(recipientId);
        if (userNotifications == null) {
            return 0;
        }
        return (int) userNotifications.stream()
                .filter(n -> !n.isRead())
                .count();
    }
}