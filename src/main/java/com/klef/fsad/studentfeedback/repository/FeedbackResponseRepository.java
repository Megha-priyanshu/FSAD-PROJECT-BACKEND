package com.klef.fsad.studentfeedback.repository;

import com.klef.fsad.studentfeedback.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long>
{
    List<FeedbackResponse> findByFormId(Long formId);
    List<FeedbackResponse> findByStudentId(Long studentId);
    boolean existsByFormIdAndStudentId(Long formId, Long studentId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("DELETE FROM FeedbackResponse r WHERE r.formId = :formId")
    void deleteByFormId(Long formId);
}
