package com.klef.fsad.studentfeedback.service;

import com.klef.fsad.studentfeedback.entity.FeedbackAnswer;
import com.klef.fsad.studentfeedback.entity.FeedbackResponse;
import com.klef.fsad.studentfeedback.repository.FeedbackResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackResponseServiceImpl implements FeedbackResponseService
{
    @Autowired private FeedbackResponseRepository repo;

    @Override
    public FeedbackResponse submit(FeedbackResponse r)
    {
        r.setSubmittedAt(LocalDate.now().toString());
        if (r.getIsAnonymous() == null) r.setIsAnonymous(false);
        if (r.getAnswers() != null)
            for (FeedbackAnswer a : r.getAnswers())
                if (a.getValue() == null) a.setValue("");
        return repo.save(r);
    }

    @Override public List<FeedbackResponse> getByFormId(Long formId) { return repo.findByFormId(formId); }
    @Override public List<FeedbackResponse> getByStudentId(Long studentId) { return repo.findByStudentId(studentId); }
    @Override public List<FeedbackResponse> getAll() { return repo.findAll(); }
    @Override public boolean hasStudentResponded(Long formId, Long studentId) { return repo.existsByFormIdAndStudentId(formId, studentId); }
}
