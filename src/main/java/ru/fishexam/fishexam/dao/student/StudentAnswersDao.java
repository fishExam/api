package ru.fishexam.fishexam.dao.student;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.student.StudentAnswers;

import java.util.List;
import java.util.Optional;

public class StudentAnswersDao {
    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "student_answers";

    public StudentAnswersDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void save(Long homeworkUserId, Long taskId, String studentAnswer, Boolean isCorrect) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (homework_user_id, task_id, student_answer, is_correct)" +
                                "VALUES (?, ?, ?, ?)",
                        tableName
                ),
                homeworkUserId, taskId, studentAnswer, isCorrect
        );
    }

    public void update(StudentAnswers studentAnswers) {
        mainDb.update(
                String.format("""
                                    INSERT INTO %s (homework_user_id, task_id, student_answer, is_correct, feedback, update_time)
                                    VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                                    ON CONFLICT (student_answers_id) DO UPDATE SET
                                    homework_user_id = EXCLUDED.homework_user_id,
                                    task_id = EXCLUDED.task_id,
                                    student_answer = EXCLUDED.student_answer,
                                    is_correct = EXCLUDED.is_correct,
                                    feedback = EXCLUDED.feedback,
                                    update_time = CURRENT_TIMESTAMP
                                """,
                        tableName
                ),
                studentAnswers.getHomeworkUserId(),
                studentAnswers.getTaskId(),
                studentAnswers.getStudentAnswer(),
                studentAnswers.getIsCorrect(),
                studentAnswers.getFeedback()
        );
    }

    public Optional<StudentAnswers> getById(Long studentAnswerId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * FROM %s
                                WHERE student_answers_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var result = new StudentAnswers();
                    result.setStudentAnswersId(rs.getLong("student_answers_id"));
                    result.setHomeworkUserId(rs.getLong("homework_user_id"));
                    result.setTaskId(rs.getLong("task_id"));
                    result.setStudentAnswer(rs.getString("student_answer"));
                    result.setCorrect(rs.getBoolean("is_correct"));
                    result.setFeedback(rs.getString("feedback"));
                    return result;
                },
                studentAnswerId
        );
    }

    public List<StudentAnswers> getAnswersByStudentId(Long studentId) {
        return mainDb.query(
                String.format(
                        """
                                SELECT sa.*, hu.student_id
                                FROM %s sa
                                JOIN homework_user hu ON sa.homework_user_id = hu.homework_user_id
                                WHERE hu.student_id = ?
                                ORDER BY sa.update_time DESC
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var result = new StudentAnswers();
                    result.setStudentAnswersId(rs.getLong("student_answers_id"));
                    result.setHomeworkUserId(rs.getLong("homework_user_id"));
                    result.setTaskId(rs.getLong("task_id"));
                    result.setStudentAnswer(rs.getString("student_answer"));
                    result.setCorrect(rs.getBoolean("is_correct"));
                    result.setFeedback(rs.getString("feedback"));
                    return result;
                },
                studentId
        );
    }

    public List<StudentAnswers> getHomeworkByStudentId(Long studentId, Long homeworkId) {
        return mainDb.query(
                String.format(
                        """
                                SELECT sa.*
                                FROM %s sa
                                JOIN homework_user hu ON sa.homework_user_id = hu.homework_user_id
                                WHERE hu.student_id = ? AND hu.homework_id = ?
                                ORDER BY sa.update_time DESC
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var result = new StudentAnswers();
                    result.setStudentAnswersId(rs.getLong("student_answers_id"));
                    result.setHomeworkUserId(rs.getLong("homework_user_id"));
                    result.setTaskId(rs.getLong("task_id"));
                    result.setStudentAnswer(rs.getString("student_answer"));
                    result.setCorrect(rs.getBoolean("is_correct"));
                    result.setFeedback(rs.getString("feedback"));
                    return result;
                },
                studentId,
                homeworkId
        );
    }
}
