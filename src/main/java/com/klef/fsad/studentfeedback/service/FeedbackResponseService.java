package com.klef.fsad.studentfeedback.service;

import com.klef.fsad.studentfeedback.entity.FeedbackResponse;
import java.util.List;

public interface FeedbackResponseService
{
    FeedbackResponse submit(FeedbackResponse response);
    List<FeedbackResponse> getByFormId(Long formId);
    List<FeedbackResponse> getByStudentId(Long studentId);
    List<FeedbackResponse> getAll();
    boolean hasStudentResponded(Long formId, Long studentId);
}
