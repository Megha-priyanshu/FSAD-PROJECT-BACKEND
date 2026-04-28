package com.klef.fsad.studentfeedback.controller;

import com.klef.fsad.studentfeedback.entity.User;
import com.klef.fsad.studentfeedback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userapi")
@CrossOrigin("*")
@Tag(name = "User API", description = "Register and login for Admin and Student roles")
public class UserController
{
    @Autowired private UserService userService;

    @Operation(summary = "Health check")
    @GetMapping("/")
    public String home() { return "User API is running"; }

    @Operation(summary = "Register a new user", description = "Role must be admin or student. Students should include rollNumber.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Account created"), @ApiResponse(responseCode = "409", description = "Email already exists") })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "Register Admin", value = "{\"name\":\"Dr. Admin Singh\",\"email\":\"admin@university.edu\",\"password\":\"admin123\",\"role\":\"admin\"}"),
            @ExampleObject(name = "Register Student", value = "{\"name\":\"Riya Sharma\",\"email\":\"riya@student.university.edu\",\"password\":\"student123\",\"role\":\"student\",\"rollNumber\":\"21060145\"}")
        }))
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user)
    {
        try {
            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null)
                return ResponseEntity.badRequest().body("Name, email, password and role are required");
            user.setEmail(user.getEmail().trim().toLowerCase());
            user.setName(user.getName().trim());
            user.setRole(user.getRole().trim().toLowerCase());
            String msg = userService.register(user);
            if (msg.contains("already exists")) return ResponseEntity.status(409).body(msg);
            return ResponseEntity.status(201).body(msg);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Login", description = "Works for both admin and student. Returns user info with role.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Login successful"), @ApiResponse(responseCode = "401", description = "Invalid credentials") })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "Admin Login", value = "{\"email\":\"admin@university.edu\",\"password\":\"admin123\"}"),
            @ExampleObject(name = "Student Login", value = "{\"email\":\"riya@student.university.edu\",\"password\":\"student123\"}")
        }))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body)
    {
        try {
            String email = body.get("email");
            String password = body.get("password");
            if (email == null || password == null)
                return ResponseEntity.badRequest().body("Email and password are required");
            User user = userService.login(email.trim().toLowerCase(), password.trim());
            if (user == null) return ResponseEntity.status(401).body("Invalid email or password.");
            Map<String, Object> info = new HashMap<>();
            info.put("id", user.getId());
            info.put("name", user.getName());
            info.put("email", user.getEmail());
            info.put("role", user.getRole());
            info.put("avatar", user.getAvatar());
            info.put("rollNumber", user.getRollNumber());
            info.put("enrolledCourses", user.getEnrolledCourses());
            return ResponseEntity.ok(info);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get all students")
    @GetMapping("/students/all")
    public ResponseEntity<?> getAllStudents()
    {
        try { return ResponseEntity.ok(userService.getAllByRole("student")); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get recently signed-in students", description = "Only students who have logged in at least once")
    @GetMapping("/students/recent")
    public ResponseEntity<?> getRecentStudents()
    {
        try { return ResponseEntity.ok(userService.getRecentlySignedIn("student")); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get all admins")
    @GetMapping("/admins/all")
    public ResponseEntity<?> getAllAdmins()
    {
        try { return ResponseEntity.ok(userService.getAllByRole("admin")); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/getbyid")
    public ResponseEntity<?> getById(@Parameter(description = "User ID", example = "1") @RequestParam Long id)
    {
        try {
            User user = userService.getById(id);
            if (user == null) return ResponseEntity.status(404).body("User not found");
            Map<String, Object> info = new HashMap<>();
            info.put("id", user.getId());
            info.put("name", user.getName());
            info.put("email", user.getEmail());
            info.put("role", user.getRole());
            info.put("avatar", user.getAvatar());
            info.put("rollNumber", user.getRollNumber());
            info.put("enrolledCourses", user.getEnrolledCourses());
            return ResponseEntity.ok(info);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }
}
