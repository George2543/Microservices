package com.university.grading.service;

import com.university.grading.model.Grade;
import com.university.grading.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradingService {
    private final GradeRepository gradeRepository;

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));
    }

    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    public Grade createGrade(Grade grade) {
        calculateLetterGrade(grade);
        return gradeRepository.save(grade);
    }

    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = getGradeById(id);
        grade.setStudentId(gradeDetails.getStudentId());
        grade.setCourseId(gradeDetails.getCourseId());
        grade.setGradeValue(gradeDetails.getGradeValue());
        calculateLetterGrade(grade);
        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    private void calculateLetterGrade(Grade grade) {
        double value = grade.getGradeValue();
        if (value >= 90) {
            grade.setLetterGrade("A");
        } else if (value >= 80) {
            grade.setLetterGrade("B");
        } else if (value >= 70) {
            grade.setLetterGrade("C");
        } else if (value >= 60) {
            grade.setLetterGrade("D");
        } else {
            grade.setLetterGrade("F");
        }
    }
}
