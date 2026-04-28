package com.klef.fsad.studentfeedback.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "feedback_form_table")
public class FeedbackForm
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(length = 20)
    private String deadline;

    @Column(name = "created_at", length = 20)
    private String createdAt;

    @Column(name = "allow_anonymous", nullable = false)
    private Boolean allowAnonymous = false;

    @Column(length = 100)
    private String category;

    @Column(length = 20)
    private String priority;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "form_id")
    @OrderBy("questionOrder ASC")
    private List<FeedbackQuestion> questions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Boolean getAllowAnonymous() { return allowAnonymous; }
    public void setAllowAnonymous(Boolean allowAnonymous) { this.allowAnonymous = allowAnonymous; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public List<FeedbackQuestion> getQuestions() { return questions; }
    public void setQuestions(List<FeedbackQuestion> questions) { this.questions = questions; }
}
