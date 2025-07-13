package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.HomeworkModel;

import java.util.Optional;

public class HomeworkModelDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "homework";

    public HomeworkModelDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
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
                    var homeworkModel = new HomeworkModel();
                    homeworkModel.setHomeworkId(homeworkId);
                    homeworkModel.setAuthorId(rs.getLong("author_id"));
                    homeworkModel.setDescription(rs.getString("description"));
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