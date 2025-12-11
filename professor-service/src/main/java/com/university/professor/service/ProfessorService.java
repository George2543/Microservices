package com.university.professor.service;

import com.university.professor.model.CourseAssignment;
import com.university.professor.model.Professor;
import com.university.professor.repository.CourseAssignmentRepository;
import com.university.professor.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found with id: " + id));
    }

    public Professor createProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor updateProfessor(Long id, Professor professorDetails) {
        Professor professor = getProfessorById(id);
        professor.setFirstName(professorDetails.getFirstName());
        professor.setLastName(professorDetails.getLastName());
        professor.setEmail(professorDetails.getEmail());
        professor.setDepartment(professorDetails.getDepartment());
        professor.setPhone(professorDetails.getPhone());
        return professorRepository.save(professor);
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public List<CourseAssignment> getProfessorAssignments(Long professorId) {
        return courseAssignmentRepository.findByProfessorId(professorId);
    }

    public CourseAssignment assignCourse(CourseAssignment assignment) {
        return courseAssignmentRepository.save(assignment);
    }

    public void deleteAssignment(Long assignmentId) {
        courseAssignmentRepository.deleteById(assignmentId);
    }
}
