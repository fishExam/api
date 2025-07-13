package ru.fishexam.fishexam.dao;

import java.util.Optional;

import ru.fishexam.fishexam.dto.StudentProfile;

public class StudentDao {
    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "students";

    public StudentDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(StudentProfile studentProfile) {
        System.out.println(
                studentProfile.getTasksCount());
        mainDb.update(
                String.format(
                        """
                        INSERT INTO %s (student_id, surname, first_name, patronymic, phone, email,
                        birth, telegram_id, parent_id, tasks_count)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON CONFLICT (student_id) DO UPDATE SET
                            surname = EXCLUDED.surname,
                            first_name = EXCLUDED.first_name,
                            patronymic = EXCLUDED.patronymic,
                            phone = EXCLUDED.phone,
                            email = EXCLUDED.email,
                            birth = EXCLUDED.birth,
                            telegram_id = EXCLUDED.telegram_id,
                            parent_id = EXCLUDED.parent_id,
                            tasks_count = EXCLUDED.tasks_count
                        """,
                        tableName
                ),
                studentProfile.getStudentId(),
                studentProfile.getSurname(),
                studentProfile.getFirstName(),
                studentProfile.getPatronymic(),
                studentProfile.getPhone(),
                studentProfile.getEmail(),
                studentProfile.getBirth(),
                studentProfile.getTelegramId(),
                studentProfile.getParentId(),
                studentProfile.getTasksCount()
        );
    }

    public Optional<StudentProfile> getById(Long userId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE student_id = ?
                        """,
                        tableName
                ),
                (rs, num) -> {
                    var studentProfile = new StudentProfile();
                    studentProfile.setStudentId(userId);
                    studentProfile.setSurname(rs.getString("surname"));
                    studentProfile.setFirstName(rs.getString("first_name"));
                    studentProfile.setPatronymic(rs.getString("patronymic"));
                    studentProfile.setPhone(rs.getString("phone"));
                    studentProfile.setEmail(rs.getString("email"));
                    studentProfile.setBirth(rs.getDate("birth").toLocalDate());
                    studentProfile.setTelegramId(rs.getString("telegram_id"));
                    studentProfile.setParentId(rs.getString("parent_id"));
                    studentProfile.setTasksCount(rs.getInt("tasks_count"));
                    return studentProfile;
                },
                userId
        );
    }
}
