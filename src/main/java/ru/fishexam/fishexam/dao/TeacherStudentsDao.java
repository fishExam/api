package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.TeacherStudentsRelations;

import java.util.List;
import java.util.Optional;

public class TeacherStudentsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "teacher_students";

    public TeacherStudentsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(TeacherStudentsRelations teacherStudentsRelations) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (teacher_id, student_id)
                            VALUES (?, ?)
                            ON CONFLICT (teacher_students_id) DO UPDATE SET
                            teacher_id = EXCLUDED.teacher_id,
                            student_id = EXCLUDED.student_id
                        """,
                tableName
                ),
                teacherStudentsRelations.getTeacherId(),
                teacherStudentsRelations.getStudentId()
        );
    }

    public Optional<TeacherStudentsRelations> getById(Long userId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                            SELECT * from %s
                            WHERE teacher_students_id = ?
                            """,
                        tableName
                ),
                (rs, num) -> {
                    var teacherStudentsRelations = new TeacherStudentsRelations();
                    teacherStudentsRelations.setTeacherStudentsId(userId);
                    teacherStudentsRelations.setTeacherId(rs.getLong("teacher_id"));
                    teacherStudentsRelations.setStudentId(rs.getLong("student_id"));
                    return teacherStudentsRelations;
                },
                userId
        );
    }

    public List<Long> getStudentsByTeacherId(Long teacherId) {
        return mainDb.query(
                String.format(
                        """
                            SELECT student_id from %s
                            WHERE teacher_id = ?
                            """,
                        tableName
                ),
                (rs, num) -> rs.getLong("student_id"),
                teacherId
        );
    }
}