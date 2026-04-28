package com.klef.fsad.studentfeedback.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback_answer_table")
public class FeedbackAnswer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "response_id", nullable = false)
    private Long responseId;

    @Column(name = "question_id", nullable = false)
    private Long qId;

    @Column(name = "answer_value", length = 2000)
    private String value;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getResponseId() { return responseId; }
    public void setResponseId(Long responseId) { this.responseId = responseId; }
    public Long getQId() { return qId; }
    public void setQId(Long qId) { this.qId = qId; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
