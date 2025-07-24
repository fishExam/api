package ru.fishexam.fishexam.dao.student;

import java.util.Optional;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.student.StudentProfile;

public class StudentDao {
    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "students";

    public StudentDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Boolean existsByUsername(String username) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(*) from %s
                        WHERE surname = ?
                        """,
                        tableName
                ),
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    public void update(StudentProfile studentProfile) {
        System.out.println(
                studentProfile.getTasksCount());
        mainDb.update(
                String.format(
                        """
                        INSERT INTO %s (user_id, surname, first_name, patronymic, phone, email,
                        birth, telegram_id, parent_id, tasks_count)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON CONFLICT (user_id) DO UPDATE SET
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
                studentProfile.getUserId(),
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
                    var studentProfile = new StudentProfile(userId,
                            rs.getString("surname"),
                            rs.getString("first_name"),
                            rs.getString("patronymic"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getDate("birth").toLocalDate(),
                            rs.getString("telegram_id"),
                            rs.getString("parent_id"),
                            rs.getInt("tasks_count"));
                    return studentProfile;
                },
                userId
        );
    }
}
