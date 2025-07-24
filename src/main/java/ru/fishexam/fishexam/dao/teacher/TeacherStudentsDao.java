package ru.fishexam.fishexam.dao.teacher;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.hobby.HobbyModel;
import ru.fishexam.fishexam.dto.student.StudentProfile;
import ru.fishexam.fishexam.dto.teacher.TeacherStudentsRelations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TeacherStudentsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "teacher_students";

    public TeacherStudentsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public List<StudentProfile> findStudentsByTeacher(Long username) {
        return mainDb.query(
                String.format(
                        """
                        SELECT * from students st
                        JOIN %s th ON th.student_id = st.user_id
                        WHERE th.teacher_id = ?
                        """,
                tableName
                ),
                (rs, num) -> new StudentProfile(
                        rs.getLong("user_id"),
                        rs.getString("surname"),
                        rs.getString("first_name"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getDate("birth").toLocalDate(),
                        rs.getString("telegram_id"),
                        rs.getString("parent_id"),
                        rs.getInt("tasks_count")
                ),
                username
        );
    }

    public Boolean existsByUsername(Long studentId, Long teacherId) {
            var count = mainDb.queryForObject(
                    String.format(
                            """
                            SELECT COUNT(*) from %s
                            WHERE student_id = ? AND teacher_id = ?
                            """,
                            tableName
                    ),
                    Integer.class,
                    studentId, teacherId
            );
            return count != null && count > 0;
        }

        public List<StudentProfile> save(TeacherStudentsRelations teacherStudentsRelations) {
            mainDb.update(
                    String.format(
                            "INSERT INTO %s (teacher_id, student_id)" +
                                    "VALUES (?, ?)",
                            tableName
                    ),
                    teacherStudentsRelations.getTeacherId(),
                    teacherStudentsRelations.getStudentId()
            );

            return findStudentsByTeacher(teacherStudentsRelations.getTeacherId());
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