package ru.fishexam.fishexam.dao.student;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Optional;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
                        WHERE username = ?
                        """,
                        tableName
                ),
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    public StudentProfile save(StudentProfile studentProfile) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        mainDb.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    String.format(
                            """
                            INSERT INTO %s
                            (username, firstName, patronymic, phone, email, birth, telegramId, parent_id, tasks_count)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """,
                            tableName
                    ),
                    new String[] {"user_id"}
            );
            ps.setString(1, studentProfile.getUsername());
            ps.setString(2, studentProfile.getFirstName());
            ps.setString(3, studentProfile.getPatronymic());
            ps.setString(4, studentProfile.getPhone());
            ps.setString(5, studentProfile.getEmail());
            ps.setDate(6, Date.valueOf(studentProfile.getBirth()));
            ps.setString(7, studentProfile.getTelegramId());
            ps.setObject(8, studentProfile.getParentId());
            ps.setInt(9, studentProfile.getTasksCount());
            return ps;
        }, keyHolder);

        studentProfile.setUserId(keyHolder.getKey().longValue());

        return studentProfile;
    }

    public void update(StudentProfile studentProfile) {
        mainDb.update(
                String.format(
                        """
                        INSERT INTO %s (user_id, username, firstName, patronymic, phone, email,
                        birth, telegramId, parent_id, tasks_count)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON CONFLICT (user_id) DO UPDATE SET
                            username = EXCLUDED.username,
                            firstName = EXCLUDED.firstName,
                            patronymic = EXCLUDED.patronymic,
                            phone = EXCLUDED.phone,
                            email = EXCLUDED.email,
                            birth = EXCLUDED.birth,
                            telegramId = EXCLUDED.telegramId,
                            parent_id = EXCLUDED.parent_id,
                            tasks_count = EXCLUDED.tasks_count
                        """,
                        tableName
                ),
                studentProfile.getUserId(),
                studentProfile.getUsername(),
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
                            rs.getString("username"),
                            rs.getString("firstName"),
                            rs.getString("patronymic"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getDate("birth").toLocalDate(),
                            rs.getString("telegramId"),
                            rs.getString("parent_id"),
                            rs.getInt("tasks_count"));
                    return studentProfile;
                },
                userId
        );
    }
}
