package com.university.student.service;

import com.university.student.model.Grade;
import com.university.student.model.Student;
import com.university.student.repository.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;

    @Value("${services.grading-service.url:http://grading-service:8084}")
    private String gradingServiceUrl;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setPhone(studentDetails.getPhone());
        student.setAddress(studentDetails.getAddress());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @CircuitBreaker(name = "gradingService", fallbackMethod = "getGradesFallback")
    public List<Grade> getStudentGrades(Long studentId) {
        log.info("Calling Grading Service for student: {}", studentId);
        try {
            String url = gradingServiceUrl + "/api/grades/student/" + studentId;
            Grade[] grades = restTemplate.getForObject(url, Grade[].class);
            return grades != null ? List.of(grades) : new ArrayList<>();
        } catch (Exception e) {
            log.error("Error calling grading service: {}", e.getMessage());
            throw e;
        }
    }

    public List<Grade> getGradesFallback(Long studentId, Exception e) {
        log.warn("Circuit breaker activated for student: {}. Error: {}", studentId, e.getMessage());
        // Return empty list or cached data
        return new ArrayList<>();
    }
}
