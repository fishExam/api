package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.HobbyStudentRelations;
import ru.fishexam.fishexam.dto.TeacherProfile;

import java.util.Optional;

public class HobbyStudentRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "hobby_student";

    public HobbyStudentRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(HobbyStudentRelations hobbyStudentRelations) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (hobby_id, student_id)
                            VALUES (?, ?)
                            ON CONFLICT (hobby_student_id) DO UPDATE SET
                            hobby_id = EXCLUDED.hobby_id,
                            student_id = EXCLUDED.student_id
                        """,
                        tableName
                ),
                hobbyStudentRelations.getHobbyId(),
                hobbyStudentRelations.getStydentId()
        );
    }

    public Optional<HobbyStudentRelations> getById(Long hobbyStudentId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE hobby_student_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var hobbyStudentRelations = new HobbyStudentRelations();
                    hobbyStudentRelations.setHobbyStydentId(hobbyStudentId);
                    hobbyStudentRelations.setHobbyId(rs.getLong("hobby_id"));
                    hobbyStudentRelations.setStydentId(rs.getLong("student_id"));
                    return hobbyStudentRelations;
                },
                hobbyStudentId
        );
    }
}