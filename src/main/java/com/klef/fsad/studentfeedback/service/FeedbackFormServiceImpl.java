package com.klef.fsad.studentfeedback.service;

import com.klef.fsad.studentfeedback.entity.FeedbackForm;
import com.klef.fsad.studentfeedback.entity.FeedbackQuestion;
import com.klef.fsad.studentfeedback.repository.FeedbackFormRepository;
import com.klef.fsad.studentfeedback.repository.FeedbackResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackFormServiceImpl implements FeedbackFormService
{
    @Autowired private FeedbackFormRepository formRepo;
    @Autowired private FeedbackResponseRepository responseRepo;

    @Override public List<FeedbackForm> getAllForms() { return formRepo.findAll(); }
    @Override public List<FeedbackForm> getActiveForms() { return formRepo.findByIsActive(true); }
    @Override public FeedbackForm getById(Long id) { return formRepo.findById(id).orElse(null); }

    @Override
    public FeedbackForm createForm(FeedbackForm form)
    {
        form.setIsActive(true);
        form.setCreatedAt(LocalDate.now().toString());
        if (form.getAllowAnonymous() == null) form.setAllowAnonymous(false);
        if (form.getPriority() == null) form.setPriority("medium");
        List<FeedbackQuestion> qs = form.getQuestions();
        if (qs != null) for (int i = 0; i < qs.size(); i++) qs.get(i).setQuestionOrder(i + 1);
        return formRepo.save(form);
    }

    @Override
    public String toggleStatus(Long id)
    {
        FeedbackForm f = formRepo.findById(id).orElse(null);
        if (f == null) return "Form not found";
        f.setIsActive(!f.getIsActive());
        formRepo.save(f);
        return f.getIsActive() ? "Form activated" : "Form deactivated";
    }

    @Override
    @Transactional
    public String deleteForm(Long id)
    {
        if (!formRepo.existsById(id)) return "Form not found";
        responseRepo.deleteByFormId(id);
        formRepo.deleteById(id);
        return "Form deleted successfully";
    }
}
