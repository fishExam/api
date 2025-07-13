package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.HomeworkUserRelations;

import java.util.List;
import java.util.Optional;

public class HomeworkUserRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "homework_user";

    public HomeworkUserRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(HomeworkUserRelations homeworkUserRealations) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (homework_id, student_id)
                            VALUES (?, ?)
                            ON CONFLICT (homework_user_id) DO UPDATE SET
                            homework_id = EXCLUDED.homework_id,
                            student_id = EXCLUDED.student_id
                        """,
                        tableName
                ),
                homeworkUserRealations.getHomeworkId(),
                homeworkUserRealations.getStudentId()
        );
    }

    public Optional<HomeworkUserRelations> getById(Long homeworkUserId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE homework_user_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var homeworkUserRealations = new HomeworkUserRelations();
                    homeworkUserRealations.setHomeworkUserId(homeworkUserId);
                    homeworkUserRealations.setHomeworkId(rs.getLong("homework_id"));
                    homeworkUserRealations.setStudentId(rs.getLong("student_id"));
                    return homeworkUserRealations;
                },
                homeworkUserId
        );
    }

    public List<HomeworkUserRelations> getByStudentId(Long studentId) {
        return mainDb.query(
                String.format(
                        """
                                SELECT * from %s
                                WHERE student_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var homeworkUserRealations = new HomeworkUserRelations();
                    homeworkUserRealations.setHomeworkUserId(rs.getLong("homework_user_id"));
                    homeworkUserRealations.setHomeworkId(rs.getLong("homework_id"));
                    homeworkUserRealations.setStudentId(studentId);
                    return homeworkUserRealations;
                },
                studentId
        );
    }

    public Long getByStudentIdAndHomeworkId(Long userId, Long homeworkId) {
        return mainDb.queryForObject(
                String.format(
                        """
                                SELECT homework_user_id from %s
                                WHERE homework_id = ? and student_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> rs.getLong("homework_user_id"),
                homeworkId, userId
        );
    }
}