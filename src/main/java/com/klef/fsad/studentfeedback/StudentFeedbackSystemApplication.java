package com.klef.fsad.studentfeedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentFeedbackSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StudentFeedbackSystemApplication.class, args);
    }

    @GetMapping("/")
    public String home()
    {
        return "✅ StudentFeedback System API is running! " +
               "Visit /swagger-ui.html for API documentation.";
    }

    @GetMapping("/health")
    public String health()
    {
        return "UP";
    }
}