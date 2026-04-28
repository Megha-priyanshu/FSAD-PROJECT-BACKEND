package com.klef.fsad.studentfeedback.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback_question_table")
public class FeedbackQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "form_id", nullable = false)
    private Long formId;

    @Column(name = "question_order")
    private Integer questionOrder;

    @Column(length = 1000, nullable = false)
    private String text;

    @Column(length = 30, nullable = false)
    private String type;

    @Column(nullable = false)
    private Boolean required;

    @Column(length = 100)
    private String category;

    @Column(length = 1000)
    private String options;

    private Integer minValue;
    private Integer maxValue;
    private Integer stepValue;

    @Column(length = 50)
    private String minLabel;

    @Column(length = 50)
    private String maxLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFormId() { return formId; }
    public void setFormId(Long formId) { this.formId = formId; }
    public Integer getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(Integer questionOrder) { this.questionOrder = questionOrder; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    public Integer getMinValue() { return minValue; }
    public void setMinValue(Integer minValue) { this.minValue = minValue; }
    public Integer getMaxValue() { return maxValue; }
    public void setMaxValue(Integer maxValue) { this.maxValue = maxValue; }
    public Integer getStepValue() { return stepValue; }
    public void setStepValue(Integer stepValue) { this.stepValue = stepValue; }
    public String getMinLabel() { return minLabel; }
    public void setMinLabel(String minLabel) { this.minLabel = minLabel; }
    public String getMaxLabel() { return maxLabel; }
    public void setMaxLabel(String maxLabel) { this.maxLabel = maxLabel; }
}
