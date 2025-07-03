package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.TeacherProfile;

public class TeacherDao {

    private final PostgreSqlJdbcTemplate postgreSqlJdbcTemplate;
    private final String tableName = "teachers";

    public TeacherDao(PostgreSqlJdbcTemplate postgreSqlJdbcTemplate) {
        this.postgreSqlJdbcTemplate = postgreSqlJdbcTemplate;
    }

    public void update(TeacherProfile teacherProfile) {
        postgreSqlJdbcTemplate.update(
                String.format(
                        "INSERT INTO %s (user_id, username, email, name) VALUES (?, ?, ?, ?)",
                        tableName
                ),
                teacherProfile.getUserId(),
                teacherProfile.getUsername(),
                teacherProfile.getEmail(),
                teacherProfile.getName()
        );
    }
}
