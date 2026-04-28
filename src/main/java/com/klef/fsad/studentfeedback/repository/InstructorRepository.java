package com.klef.fsad.studentfeedback.repository;

import com.klef.fsad.studentfeedback.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {}
