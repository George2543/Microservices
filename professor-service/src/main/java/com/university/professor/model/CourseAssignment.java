package com.university.professor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long professorId;
    private Long courseId;
    private String semester;
}
