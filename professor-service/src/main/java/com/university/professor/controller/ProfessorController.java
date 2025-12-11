package com.university.professor.controller;

import com.university.professor.model.CourseAssignment;
import com.university.professor.model.Professor;
import com.university.professor.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfessorController {
    private final ProfessorService professorService;

    @GetMapping
    public List<Professor> getAllProfessors() {
        return professorService.getAllProfessors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }

    @PostMapping
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {
        return ResponseEntity.ok(professorService.createProfessor(professor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        return ResponseEntity.ok(professorService.updateProfessor(id, professor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<CourseAssignment>> getProfessorAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getProfessorAssignments(id));
    }

    @PostMapping("/assignments")
    public ResponseEntity<CourseAssignment> assignCourse(@RequestBody CourseAssignment assignment) {
        return ResponseEntity.ok(professorService.assignCourse(assignment));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        professorService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
