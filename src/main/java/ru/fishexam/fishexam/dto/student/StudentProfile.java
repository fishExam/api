package ru.fishexam.fishexam.dto.student;

import jakarta.annotation.Nullable;
import ru.fishexam.fishexam.dto.user.UserProfile;

import java.time.LocalDate;

public class StudentProfile extends UserProfile {
    @Nullable
    private String parentId;
    @Nullable
    private Integer tasksCount;

    public StudentProfile(Long userId, String surname, String firstName, @Nullable String patronymic, String phone, @Nullable String email, LocalDate birth, String telegramId, @Nullable String parentId, @Nullable Integer tasksCount) {
        super(userId, surname, firstName, patronymic, phone, email, birth, telegramId);
        this.parentId = parentId;
        this.tasksCount = tasksCount;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable String parent_id) {
        this.parentId = parent_id;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasks_count) {
        this.tasksCount = tasks_count;
    }
}
