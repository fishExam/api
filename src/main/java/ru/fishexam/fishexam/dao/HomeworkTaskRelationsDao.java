package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.HomeworkTaskRelations;

import java.util.Optional;

public class HomeworkTaskRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "homework_task";

    public HomeworkTaskRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
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
                    var homeworkTaskRelations = new HomeworkTaskRelations();
                    homeworkTaskRelations.setHomeworkTaskId(homeworkTaskId);
                    homeworkTaskRelations.setHomeworkId(rs.getLong("homework_id"));
                    homeworkTaskRelations.setTaskId(rs.getLong("task_id"));
                    return homeworkTaskRelations;
                },
                homeworkTaskId
        );
    }
}