package com.klef.fsad.studentfeedback.service;

import com.klef.fsad.studentfeedback.entity.FeedbackForm;
import java.util.List;

public interface FeedbackFormService
{
    List<FeedbackForm> getAllForms();
    List<FeedbackForm> getActiveForms();
    FeedbackForm getById(Long id);
    FeedbackForm createForm(FeedbackForm form);
    String toggleStatus(Long id);
    String deleteForm(Long id);
}
