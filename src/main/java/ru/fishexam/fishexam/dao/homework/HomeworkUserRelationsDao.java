package ru.fishexam.fishexam.dao.homework;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.homework.HomeworkUserRelations;
import ru.fishexam.fishexam.dto.task.TaskModel;

import java.util.List;
import java.util.Optional;

public class HomeworkUserRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "homework_user";

    public HomeworkUserRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<HomeworkModel> findHomework(Long studentId, Long homeworkId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT h.* from %s hu 
                        JOIN homework h ON h.homework_id = hu.homework_id 
                        WHERE h.homework_id = ? AND hu.student_id = ?
                        """,
                        tableName
                ),
                (rs, num) -> new HomeworkModel(
                        rs.getLong("homework_id"),
                        rs.getLong("author_id"),
                        rs.getString("description")
                ),
                homeworkId, studentId
        );
    }

    public Boolean existsByRelation(Long studentId, Long homeworkId) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(*) from %s
                        WHERE student_id = ? AND homework_id = ?
                        """,
                        tableName
                ),
                Integer.class,
                studentId, homeworkId
        );
        return count != null && count > 0;
    }

    public Optional<HomeworkModel> save(Long studentId, Long homeworkId) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (homewotk_id, student_id)" +
                                "VALUES (?, ?)",
                        tableName
                ),
                homeworkId,
                studentId
        );

        return findHomework(studentId, homeworkId);
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