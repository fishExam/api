package ru.fishexam.fishexam.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.fishexam.fishexam.dto.StudentProfile;

public class StudentDao {
    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "students";

    public StudentDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(StudentProfile studentProfile) {
        mainDb.update(
                String.format(
                        """
                        INSERT INTO %s (user_id, username, email, name)
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (user_id) DO UPDATE SET
                            username = EXCLUDED.username,
                            email = EXCLUDED.email,
                            name = EXCLUDED.name
                        """,
                        tableName
                ),
                studentProfile.getUserId(),
                studentProfile.getUsername(),
                studentProfile.getEmail(),
                studentProfile.getName()
        );
    }

    public Optional<StudentProfile> getById(Long userId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE user_id = ?
                        """,
                        tableName
                ),
                (rs, num) -> {
                    var studentProfile = new StudentProfile();
                    studentProfile.setUserId(userId);
                    studentProfile.setName(rs.getString("name"));
                    studentProfile.setUsername(rs.getString("username"));
                    studentProfile.setEmail(rs.getString("email"));
                    return studentProfile;
                },
                userId
        );
    }
}
