package ru.fishexam.fishexam.dao.teacher;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.teacher.TeacherProfile;

import java.util.Optional;

public class TeacherDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "teachers";

    public TeacherDao(PostgreSqlJdbcTemplate mainDb) {
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

    public void update(TeacherProfile teacherProfile) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (user_id, surname, first_name, patronymic, phone, email,
                            birth, telegram_id)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                            ON CONFLICT (user_id) DO UPDATE SET
                            surname = EXCLUDED.surname,
                            first_name = EXCLUDED.first_name,
                            patronymic = EXCLUDED.patronymic,
                            phone = EXCLUDED.phone,
                            email = EXCLUDED.email,
                            birth = EXCLUDED.birth,
                            telegram_id = EXCLUDED.telegram_id
                        """,
                tableName
                ),
                teacherProfile.getUserId(),
                teacherProfile.getSurname(),
                teacherProfile.getFirstName(),
                teacherProfile.getPatronymic(),
                teacherProfile.getPhone(),
                teacherProfile.getEmail(),
                teacherProfile.getBirth(),
                teacherProfile.getTelegramId()
        );
    }

    public Optional<TeacherProfile> getById(Long userId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE user_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var teacherProfile = new TeacherProfile(userId,
                            rs.getString("surname"),
                            rs.getString("first_name"),
                            rs.getString("patronymic"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getDate("birth").toLocalDate(),
                            rs.getString("telegram_id"));
                    return teacherProfile;
                },
                userId
        );
    }
}