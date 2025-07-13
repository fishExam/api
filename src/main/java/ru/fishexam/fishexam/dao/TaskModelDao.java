package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.TaskModel;
import ru.fishexam.fishexam.dto.TaskStatistic;

import java.util.List;
import java.util.Optional;

public class TaskModelDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "task";

    public TaskModelDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
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
                    var taskModel = new TaskModel();
                    taskModel.setTaskId(taskId);
                    taskModel.setAuthorId(rs.getLong("author_id"));
                    taskModel.setTitle(rs.getString("title"));
                    taskModel.setAnswer(rs.getString("answer"));
                    return taskModel;
                },
                taskId
        );
    }
}