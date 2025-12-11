package com.university.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Double gradeValue;
    private String letterGrade;
}
