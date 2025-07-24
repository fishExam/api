package ru.fishexam.fishexam.dao.task;

import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.task.TaskModel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskModelDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "task";

    public TaskModelDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<TaskModel> findTask(Long author_id, String title) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE author_id = ? AND title = ?
                        """,
                        tableName
                ),
                (rs, num) -> new TaskModel(
                        rs.getLong("task_id"),
                        rs.getLong("author_id"),
                        rs.getString("title"),
                        rs.getString("answer")
                ),
                author_id, title
        );
    }

    public Boolean existsByTaskTeacher(Long teacherId, String title) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(*) from %s
                        WHERE author_id = ? AND title = ?
                        """,
                        tableName
                ),
                Integer.class,
                teacherId, title
        );
        return count != null && count > 0;
    }

    public TaskModel save(Long author_id, String title, String answer) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (author_id, title, answer)" +
                                "VALUES (?, ?, ?)",
                        tableName
                ),
                author_id,
                title,
                answer
        );

        return findTask(author_id, title).orElseThrow();
    }

    public void update(TaskModel taskModel) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (author_id, title, answer)
                            VALUES (?, ?, ?)
                            ON CONFLICT (task_id) DO UPDATE SET
                            author_id = EXCLUDED.author_id,
                            title = EXCLUDED.title,
                            answer = EXCLUDED.answer
                        """,
                        tableName
                ),
                taskModel.getAuthorId(),
                taskModel.getTitle(),
                taskModel.getAnswer()
        );
    }

    public Optional<TaskModel> getById(Long taskId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE task_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var taskModel = new TaskModel(
                            rs.getLong("task_id"),
                            rs.getLong("author_id"),
                            rs.getString("title"),
                            rs.getString("answer"));
                    return taskModel;
                },
                taskId
        );
    }
}