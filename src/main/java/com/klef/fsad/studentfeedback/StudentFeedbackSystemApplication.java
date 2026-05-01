package com.klef.fsad.studentfeedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class StudentFeedbackSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StudentFeedbackSystemApplication.class, args);
    }

    @GetMapping("/")
    public Map<String, Object> home()
    {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("app", "StudentFeedback System API");
        response.put("message", "Backend is running");
        response.put("status", "UP");
        response.put("version", "2.0.0");
        response.put("swagger", "https://fsad-project-backend-442w.onrender.com/swagger-ui.html");
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health()
    {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("message", "All systems operational");
        return response;
    }
}