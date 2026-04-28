package com.klef.fsad.studentfeedback.repository;

import com.klef.fsad.studentfeedback.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {}
