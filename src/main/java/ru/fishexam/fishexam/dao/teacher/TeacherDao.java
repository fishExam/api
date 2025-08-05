package ru.fishexam.fishexam.dao.teacher;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.teacher.TeacherProfile;

import java.sql.Date;
import java.sql.PreparedStatement;
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
                        WHERE username = ?
                        """,
                        tableName
                ),
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    public TeacherProfile save(TeacherProfile teacherProfile) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        mainDb.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    String.format(
                            """
                            INSERT INTO %s
                            (username, firstName, patronymic, phone, email, birth, telegramId)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                            """,
                            tableName
                    ),
                    new String[] {"user_id"}
            );
            ps.setString(1, teacherProfile.getUsername());
            ps.setString(2, teacherProfile.getFirstName());
            ps.setString(3, teacherProfile.getPatronymic());
            ps.setString(4, teacherProfile.getPhone());
            ps.setString(5, teacherProfile.getEmail());
            ps.setDate(6, Date.valueOf(teacherProfile.getBirth()));
            ps.setString(7, teacherProfile.getTelegramId());
            return ps;
        }, keyHolder);

        teacherProfile.setUserId(keyHolder.getKey().longValue());

        return teacherProfile;
    }

    public void update(TeacherProfile teacherProfile) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (user_id, username, firstName, patronymic, phone, email, birth, telegramId)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                            ON CONFLICT (user_id) DO UPDATE SET
                            username = EXCLUDED.username,
                            firstName = EXCLUDED.firstName,
                            patronymic = EXCLUDED.patronymic,
                            phone = EXCLUDED.phone,
                            email = EXCLUDED.email,
                            birth = EXCLUDED.birth,
                            telegramId = EXCLUDED.telegramId
                        """,
                tableName
                ),
                teacherProfile.getUserId(),
                teacherProfile.getUsername(),
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
                            rs.getString("username"),
                            rs.getString("firstName"),
                            rs.getString("patronymic"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getDate("birth").toLocalDate(),
                            rs.getString("telegramId"));
                    return teacherProfile;
                },
                userId
        );
    }
}