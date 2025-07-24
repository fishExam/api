package ru.fishexam.fishexam.dao.homework;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.task.TaskModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class HomeworkModelDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "homework";

    public HomeworkModelDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<HomeworkModel> findByHomework(Long authId, String description) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE author_id = ? AND description = ?
                        """,
                        tableName
                ),
                (rs, num) -> new HomeworkModel(
                        rs.getLong("homework_id"),
                        rs.getLong("author_id"),
                        rs.getString("description")
                ),
                authId, description
        );
    }

    public Boolean existsByHomework(Long authorId, String description) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(*) from %s
                        WHERE author_id = ? AND description = ?
                        """,
                        tableName
                ),
                Integer.class,
                authorId, description
        );
        return count != null && count > 0;
    }
    public Boolean existsTaskByHomework(String description) {
        String[] arr = description.split(" ");
        for (String i : arr) {
            Long taskId = Long.parseLong(i);
            var count = mainDb.queryForObject(
                    String.format(
                            """
                                    SELECT title from task
                                    WHERE task_id = ?
                                    """,
                            tableName
                    ),
                    Integer.class,
                    taskId
            );
            if (count == null && count > 0)
                return false;
        }
        return true;
    }

    public HomeworkModel save(Long teacherId, String description) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (author_id, description)" +
                                "VALUES (?, ?)",
                        tableName
                ),
                teacherId,
                description
        );

        return findByHomework(teacherId, description).orElseThrow();
    }


    public void update(HomeworkModel homeworkModel) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (author_id, description)
                            VALUES (?, ?)
                            ON CONFLICT (homework_id) DO UPDATE SET
                            author_id = EXCLUDED.author_id,
                            description = EXCLUDED.description
                        """,
                        tableName
                ),
                homeworkModel.getAuthorId(),
                homeworkModel.getDescription()
        );
    }

    public Optional<HomeworkModel> getById(Long homeworkId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE homework_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var homeworkModel = new HomeworkModel(homeworkId,
                            rs.getLong("author_id"),
                            rs.getString("description"));
                    return homeworkModel;
                },
                homeworkId
        );
    }
    public Long getByInfo(Long authorId, String description) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT homework_id from %s
                                WHERE author_id = ? AND description = ?
                                """,
                        tableName
                ),
                (rs, num) -> rs.getLong("homework_id"),
                authorId, description).orElse(null);
    }
}