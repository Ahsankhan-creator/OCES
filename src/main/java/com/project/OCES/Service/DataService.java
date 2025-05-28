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
    private final Map<String, List<Notification>> notifications = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initializeData() {
        // Initialize Faculty
        Faculty faculty1 = new Faculty("F001", "Dr. Syed Sameed Abbas", "syed.sameed@university.edu");
        Faculty faculty2 = new Faculty("F002", "Dr. Ali Raza", "ali.raza@university.edu");
        Faculty faculty3 = new Faculty("F003", "Dr. Zaid khan", "zaid.khann@university.edu");
        Faculty faculty4 = new Faculty("F004", "Dr. Abdullah", "abdullah.123@university.edu");
        
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
        Student student1 = new Student("S001", "Muhammad Ahsan khan", "muhammad.ahsan@student.edu");
        Student student2 = new Student("S002", "Zaid khan", "zaid.khanh@student.edu");
        Student student3 = new Student("S003", "Abdullah khan", "abdullah.khan@student.edu");
        Student student4 = new Student("S004", "Ali Raza", "ali.raza@student.edu");
        Student student5 = new Student("S005", "Malik", "malik.malik@student.edu");
        Student student6 = new Student("S006", "Adil Wagan", "adil.wagan@student.edu");
        Student student7 = new Student("S007", "Mujeed Bhoto", "mujeed.bhoto@student.edu");
        Student student8 = new Student("S008", "Ayesha Khan", "ayesha.khan@student.edu");
        Student student9 = new Student("S009", "Adbul Hadi", "abdul.hadi@student.edu");
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
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getCourseCode().equals(courseCode))
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // Notification operations
    public void addNotification(Notification notification) {
        notifications.computeIfAbsent(notification.getRecipientId(), k -> new ArrayList<>()).add(notification);
    }
    
    public List<Notification> getNotifications(String recipientId) {
        return notifications.getOrDefault(recipientId, new ArrayList<>());
    }
    
    public void markAsRead(String recipientId, String notificationId) {
        List<Notification> userNotifications = notifications.get(recipientId);
        if (userNotifications != null) {
            userNotifications.stream()
                .filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .ifPresent(n -> n.setRead(true));
        }
    }
    
    public int getUnreadNotificationCount(String recipientId) {
        return (int) notifications.getOrDefault(recipientId, new ArrayList<>())
                .stream()
                .filter(n -> !n.isRead())
                .count();
    }
}