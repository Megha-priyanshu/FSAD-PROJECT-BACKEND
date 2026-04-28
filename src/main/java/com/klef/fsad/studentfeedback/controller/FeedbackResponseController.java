package com.klef.fsad.studentfeedback.controller;

import com.klef.fsad.studentfeedback.entity.FeedbackForm;
import com.klef.fsad.studentfeedback.entity.FeedbackQuestion;
import com.klef.fsad.studentfeedback.entity.FeedbackResponse;
import com.klef.fsad.studentfeedback.service.FeedbackFormService;
import com.klef.fsad.studentfeedback.service.FeedbackResponseService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/responseapi")
@CrossOrigin("*")
@Tag(name = "Feedback Response API", description = "Submit feedback, check status, and view analytics")
public class FeedbackResponseController
{
    @Autowired private FeedbackResponseService responseService;
    @Autowired private FeedbackFormService formService;

    @Operation(summary = "Health check")
    @GetMapping("/")
    public String home() { return "Response API is running"; }

    @Operation(summary = "Submit a feedback response",
        description = "Set isAnonymous=true for anonymous. studentId can be null if anonymous.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Submitted"),
        @ApiResponse(responseCode = "409", description = "Already submitted"),
        @ApiResponse(responseCode = "404", description = "Form not found"),
        @ApiResponse(responseCode = "403", description = "Form closed")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(value = "{\n  \"formId\": 1,\n  \"studentId\": 1,\n  \"isAnonymous\": false,\n  \"timeSpent\": 285,\n  \"answers\": [\n    {\"qId\": 1, \"value\": \"4\"},\n    {\"qId\": 2, \"value\": \"8\"},\n    {\"qId\": 3, \"value\": \"Theory,Practicals\"},\n    {\"qId\": 4, \"value\": \"Agree\"},\n    {\"qId\": 5, \"value\": \"Great course!\"}\n  ]\n}")))
    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody FeedbackResponse response)
    {
        try {
            if (response.getFormId() == null)
                return ResponseEntity.badRequest().body("formId is required");
            if (!Boolean.TRUE.equals(response.getIsAnonymous()) && response.getStudentId() != null)
                if (responseService.hasStudentResponded(response.getFormId(), response.getStudentId()))
                    return ResponseEntity.status(409).body("Student has already submitted this form");
            FeedbackForm form = formService.getById(response.getFormId());
            if (form == null) return ResponseEntity.status(404).body("Form not found");
            if (!form.getIsActive()) return ResponseEntity.status(403).body("Form is no longer accepting responses");
            return ResponseEntity.status(201).body(responseService.submit(response));
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Check if student already responded")
    @GetMapping("/check")
    public ResponseEntity<?> check(
        @Parameter(description = "Form ID", example = "1") @RequestParam Long formId,
        @Parameter(description = "Student ID", example = "1") @RequestParam Long studentId)
    {
        try {
            Map<String, Boolean> result = new HashMap<>();
            result.put("hasResponded", responseService.hasStudentResponded(formId, studentId));
            return ResponseEntity.ok(result);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get all responses for a form")
    @GetMapping("/byform")
    public ResponseEntity<?> byForm(@Parameter(description = "Form ID", example = "1") @RequestParam Long formId)
    {
        try { return ResponseEntity.ok(responseService.getByFormId(formId)); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get all responses by a student")
    @GetMapping("/bystudent")
    public ResponseEntity<?> byStudent(@Parameter(description = "Student ID", example = "1") @RequestParam Long studentId)
    {
        try { return ResponseEntity.ok(responseService.getByStudentId(studentId)); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get all responses - Admin")
    @GetMapping("/all")
    public ResponseEntity<?> all()
    {
        try { return ResponseEntity.ok(responseService.getAll()); }
        catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get average ratings per question for a form")
    @GetMapping("/analytics/avgratings")
    public ResponseEntity<?> avgRatings(@Parameter(description = "Form ID", example = "1") @RequestParam Long formId)
    {
        try {
            FeedbackForm form = formService.getById(formId);
            if (form == null) return ResponseEntity.status(404).body("Form not found");
            List<FeedbackResponse> responses = responseService.getByFormId(formId);
            List<Map<String, Object>> result = new ArrayList<>();
            for (FeedbackQuestion q : form.getQuestions())
            {
                if (!"rating".equals(q.getType()) && !"nps".equals(q.getType())) continue;
                List<Double> vals = responses.stream()
                    .flatMap(r -> r.getAnswers().stream())
                    .filter(a -> a.getQId().equals(q.getId()))
                    .map(a -> { try { return Double.parseDouble(a.getValue()); } catch (Exception ex) { return null; } })
                    .filter(Objects::nonNull).collect(Collectors.toList());
                double avg = vals.isEmpty() ? 0 : vals.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("id", q.getId()); row.put("text", q.getText()); row.put("type", q.getType());
                row.put("category", q.getCategory());
                row.put("avg", Math.round(avg * 100.0) / 100.0); row.put("count", vals.size());
                result.add(row);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }

    @Operation(summary = "Get overall system statistics - Admin dashboard")
    @GetMapping("/analytics/overall")
    public ResponseEntity<?> overall()
    {
        try {
            List<FeedbackForm> forms = formService.getAllForms();
            List<FeedbackResponse> responses = responseService.getAll();
            List<Double> allRatings = responses.stream()
                .flatMap(r -> r.getAnswers().stream())
                .map(a -> { try { return Double.parseDouble(a.getValue()); } catch (Exception ex) { return null; } })
                .filter(v -> v != null && v >= 1 && v <= 5).collect(Collectors.toList());
            double avg = allRatings.isEmpty() ? 0 : allRatings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("totalForms", forms.size());
            stats.put("activeForms", forms.stream().filter(FeedbackForm::getIsActive).count());
            stats.put("totalResponses", responses.size());
            stats.put("anonymousResponses", responses.stream().filter(r -> Boolean.TRUE.equals(r.getIsAnonymous())).count());
            stats.put("avgRating", Math.round(avg * 10.0) / 10.0);
            return ResponseEntity.ok(stats);
        } catch (Exception e) { return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage()); }
    }
}
