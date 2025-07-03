package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.TeacherProfile;

public class TeacherDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "teachers";

    public TeacherDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(TeacherProfile teacherProfile) {
        mainDb.update(
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
