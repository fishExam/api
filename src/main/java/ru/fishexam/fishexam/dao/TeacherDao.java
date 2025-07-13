package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.TeacherProfile;

import java.util.Optional;

public class TeacherDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "teachers";

    public TeacherDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(TeacherProfile teacherProfile) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (teacher_id, surname, first_name, patronymic, phone, email,
                            birth, telegram_id)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                            ON CONFLICT (teacher_id) DO UPDATE SET
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
                teacherProfile.getTeacherId(),
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
                                WHERE teacher_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var teacherProfile = new TeacherProfile();
                    teacherProfile.setTeacherId(userId);
                    teacherProfile.setSurname(rs.getString("surname"));
                    teacherProfile.setFirstName(rs.getString("first_name"));
                    teacherProfile.setPatronymic(rs.getString("patronymic"));
                    teacherProfile.setPhone(rs.getString("phone"));
                    teacherProfile.setEmail(rs.getString("email"));
                    teacherProfile.setBirth(rs.getDate("birth").toLocalDate());
                    teacherProfile.setTelegramId(rs.getString("telegram_id"));
                    return teacherProfile;
                },
                userId
        );
    }
}