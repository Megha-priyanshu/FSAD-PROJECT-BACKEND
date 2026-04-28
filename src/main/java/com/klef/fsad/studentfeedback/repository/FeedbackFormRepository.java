package com.klef.fsad.studentfeedback.repository;

import com.klef.fsad.studentfeedback.entity.FeedbackForm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackFormRepository extends JpaRepository<FeedbackForm, Long>
{
    List<FeedbackForm> findByIsActive(Boolean isActive);
}
