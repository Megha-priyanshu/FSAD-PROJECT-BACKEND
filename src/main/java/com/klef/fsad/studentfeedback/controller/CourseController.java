package com.klef.fsad.studentfeedback.controller;

import com.klef.fsad.studentfeedback.entity.Course;
import com.klef.fsad.studentfeedback.entity.Instructor;
import com.klef.fsad.studentfeedback.repository.CourseRepository;
import com.klef.fsad.studentfeedback.repository.InstructorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseapi")
@CrossOrigin("*")
@Tag(name = "Course and Instructor API", description = "Manage courses and instructors")
public class CourseController
{
    @Autowired private CourseRepository courseRepo;
    @Autowired private InstructorRepository instructorRepo;

    @Operation(summary = "Get all courses")
    @GetMapping("/courses/all")
    public ResponseEntity<?> getAllCourses()
    {
        try { return ResponseEntity.ok(courseRepo.findAll()); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Add a course")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(value = "{\"name\":\"Mathematics 101\",\"code\":\"MATH101\",\"instructorId\":1,\"department\":\"Mathematics\",\"credits\":4,\"semester\":\"Spring 2026\",\"capacity\":50,\"enrolled\":48}")))
    @PostMapping("/courses/add")
    public ResponseEntity<?> addCourse(@RequestBody Course course)
    {
        try {
            if (course.getName() == null || course.getCode() == null)
                return ResponseEntity.badRequest().body("Name and code required");
            return ResponseEntity.status(201).body(courseRepo.save(course));
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Delete a course")
    @DeleteMapping("/courses/delete")
    public ResponseEntity<?> deleteCourse(@Parameter(description = "Course ID", example = "1") @RequestParam Long id)
    {
        try {
            if (!courseRepo.existsById(id)) return ResponseEntity.status(404).body("Course not found");
            courseRepo.deleteById(id);
            return ResponseEntity.ok("Course deleted");
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get all instructors")
    @GetMapping("/instructors/all")
    public ResponseEntity<?> getAllInstructors()
    {
        try { return ResponseEntity.ok(instructorRepo.findAll()); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Add an instructor")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(value = "{\"name\":\"Dr. Rajesh Kumar\",\"department\":\"Mathematics\",\"email\":\"r.kumar@university.edu\",\"courses\":\"1\",\"phone\":\"+91-9876543210\",\"office\":\"Math Building - 301\"}")))
    @PostMapping("/instructors/add")
    public ResponseEntity<?> addInstructor(@RequestBody Instructor instructor)
    {
        try {
            if (instructor.getName() == null) return ResponseEntity.badRequest().body("Name required");
            return ResponseEntity.status(201).body(instructorRepo.save(instructor));
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Delete an instructor")
    @DeleteMapping("/instructors/delete")
    public ResponseEntity<?> deleteInstructor(@Parameter(description = "Instructor ID", example = "1") @RequestParam Long id)
    {
        try {
            if (!instructorRepo.existsById(id)) return ResponseEntity.status(404).body("Instructor not found");
            instructorRepo.deleteById(id);
            return ResponseEntity.ok("Instructor deleted");
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }
}
