package com.klef.fsad.studentfeedback.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig
{
    @Bean
    public OpenAPI studentFeedbackOpenAPI()
    {
        return new OpenAPI()
            .info(new Info()
                .title("StudentFeedback System API")
                .version("2.0.0")
                .description("REST API for StudentFeedback System - register, login, anonymous responses, advanced question types, analytics.")
                .contact(new Contact().name("StudentFeedback System").email("admin@university.edu")))
            .servers(List.of(new Server().url("http://localhost:8080").description("Local Development Server")));
    }
}
