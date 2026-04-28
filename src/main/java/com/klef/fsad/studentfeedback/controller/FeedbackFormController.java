package com.klef.fsad.studentfeedback.controller;

import com.klef.fsad.studentfeedback.entity.FeedbackForm;
import com.klef.fsad.studentfeedback.service.FeedbackFormService;
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

@RestController
@RequestMapping("/formapi")
@CrossOrigin("*")
@Tag(name = "Feedback Form API", description = "Create, read, toggle and delete feedback forms with advanced question types")
public class FeedbackFormController
{
    @Autowired private FeedbackFormService formService;

    @Operation(summary = "Get all forms - Admin use")
    @GetMapping("/all")
    public ResponseEntity<?> getAllForms()
    {
        try { return ResponseEntity.ok(formService.getAllForms()); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get active forms - Student use")
    @GetMapping("/active")
    public ResponseEntity<?> getActiveForms()
    {
        try { return ResponseEntity.ok(formService.getActiveForms()); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get form by ID")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Form found"), @ApiResponse(responseCode = "404", description = "Not found") })
    @GetMapping("/getbyid")
    public ResponseEntity<?> getById(@Parameter(description = "Form ID", example = "1") @RequestParam Long id)
    {
        try {
            FeedbackForm f = formService.getById(id);
            if (f == null) return ResponseEntity.status(404).body("Form not found");
            return ResponseEntity.ok(f);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Create a feedback form",
        description = "Supports types: rating, text, nps, checkbox, likert, range, multiple_choice. Set allowAnonymous=true for anonymous submissions.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Form created"), @ApiResponse(responseCode = "400", description = "Validation failed") })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(value = "{\n  \"title\": \"FSAD Course Evaluation\",\n  \"description\": \"Rate your FSAD experience\",\n  \"type\": \"course\",\n  \"targetId\": 6,\n  \"deadline\": \"2026-06-30\",\n  \"allowAnonymous\": true,\n  \"category\": \"Teaching Effectiveness\",\n  \"priority\": \"high\",\n  \"questions\": [\n    {\"text\": \"Rate overall quality\", \"type\": \"rating\", \"required\": true, \"category\": \"Course Content\"},\n    {\"text\": \"Recommend? (0-10)\", \"type\": \"nps\", \"required\": true},\n    {\"text\": \"Which topics?\", \"type\": \"checkbox\", \"required\": false, \"options\": \"Theory,Practicals,Projects\"},\n    {\"text\": \"Comments\", \"type\": \"text\", \"required\": false}\n  ]\n}")))
    @PostMapping("/create")
    public ResponseEntity<?> createForm(@RequestBody FeedbackForm form)
    {
        try {
            if (form.getTitle() == null || form.getType() == null)
                return ResponseEntity.badRequest().body("Title and type are required");
            if (form.getQuestions() == null || form.getQuestions().isEmpty())
                return ResponseEntity.badRequest().body("At least one question required");
            return ResponseEntity.status(201).body(formService.createForm(form));
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Toggle form active or inactive")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Toggled"), @ApiResponse(responseCode = "404", description = "Not found") })
    @PutMapping("/toggle-status")
    public ResponseEntity<?> toggleStatus(@Parameter(description = "Form ID", example = "1") @RequestParam Long id)
    {
        try {
            String msg = formService.toggleStatus(id);
            if ("Form not found".equals(msg)) return ResponseEntity.status(404).body(msg);
            return ResponseEntity.ok(msg);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Delete a form and all its responses")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteForm(@Parameter(description = "Form ID", example = "1") @RequestParam Long id)
    {
        try {
            String msg = formService.deleteForm(id);
            if ("Form not found".equals(msg)) return ResponseEntity.status(404).body(msg);
            return ResponseEntity.ok(msg);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }
}
