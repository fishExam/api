package ru.fishexam.fishexam.dto.homework;

import jakarta.annotation.Nullable;

public class HomeworkModel {
    private Long homeworkId;
    private Long authorId;
    @Nullable
    private String description;

    public HomeworkModel(Long homeworkId, Long authorId, @Nullable String description) {
        this.homeworkId = homeworkId;
        this.authorId = authorId;
        this.description = description;
    }

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
