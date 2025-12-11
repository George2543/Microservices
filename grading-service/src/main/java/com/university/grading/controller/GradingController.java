package com.university.grading.controller;

import com.university.grading.model.Grade;
import com.university.grading.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GradingController {
    private final GradingService gradingService;

    @GetMapping
    public List<Grade> getAllGrades() {
        return gradingService.getAllGrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        return ResponseEntity.ok(gradingService.getGradeById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradingService.getGradesByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(gradingService.getGradesByCourseId(courseId));
    }

    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        return ResponseEntity.ok(gradingService.createGrade(grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
        return ResponseEntity.ok(gradingService.updateGrade(id, grade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradingService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
