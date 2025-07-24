package ru.fishexam.fishexam.dao.homework;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.homework.HomeworkModelRequest;
import ru.fishexam.fishexam.dto.homework.HomeworkTaskRelations;
import ru.fishexam.fishexam.dto.task.TaskModel;

import java.util.List;
import java.util.Optional;

public class HomeworkTaskRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "homework_task";

    public HomeworkTaskRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<HomeworkTaskRelations> findRelations(Long task_id, Long homeworkId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE task_id = ? AND homework_id = ?
                        """,
                        tableName
                ),
                (rs, num) -> new HomeworkTaskRelations(
                        rs.getLong("homework_task_id"),
                        rs.getLong("homework_id"),
                        rs.getLong("task_id")
                ),
                task_id, homeworkId
        );
    }

    public Boolean existsByRelations(Long taskId, Long homeworkId) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT * from %s
                        WHERE task_id = ? AND homework_id = ?
                        """,
                        tableName
                ),
                Integer.class,
                taskId, homeworkId
        );
        return count != null && count > 0;
    }

    public HomeworkTaskRelations save(Long taskId, Long homeworkId) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (task_id, homework_id)" +
                                "VALUES (?, ?)",
                        tableName
                ),
                taskId,
                homeworkId
        );

        return findRelations(taskId, homeworkId).orElseThrow();
    }

    public void update(HomeworkTaskRelations homeworkTaskRelations) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (homework_id, task_id)
                            VALUES (?, ?)
                            ON CONFLICT (homework_task_id) DO UPDATE SET
                            homework_id = EXCLUDED.homework_id,
                            task_id = EXCLUDED.task_id
                        """,
                        tableName
                ),
                homeworkTaskRelations.getHomeworkId(),
                homeworkTaskRelations.getTaskId()
        );
    }

    public Optional<HomeworkTaskRelations> getById(Long homeworkTaskId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE homework_task_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var homeworkTaskRelations = new HomeworkTaskRelations(homeworkTaskId,
                            rs.getLong("homework_id"),
                            rs.getLong("task_id"));
                    return homeworkTaskRelations;
                },
                homeworkTaskId
        );
    }
}