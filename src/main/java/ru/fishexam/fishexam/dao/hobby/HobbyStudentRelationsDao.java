package ru.fishexam.fishexam.dao.hobby;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.hobby.HobbyModel;
import ru.fishexam.fishexam.dto.hobby.HobbyStudentRelations;

import java.util.List;
import java.util.Optional;

public class HobbyStudentRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "hobby_student";

    public HobbyStudentRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public List<HobbyModel> findByStudent(Long studentId) {
        return mainDb.query(
                String.format(
                        """
                        SELECT * from hobby
                        JOIN %s h_st ON h_st.hobby_id = hobby.hobby_id
                        WHERE h_st.student_id = ?
                        """,
                        tableName
                ),
                (rs, num) -> new HobbyModel(
                        rs.getLong("hobby_id"),
                        rs.getString("topic")
                ),
                studentId
        );
    }

    public Boolean existsByUsername(Long studentId, String topic) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(hobby.topic) from %s h_st
                        JOIN hobby ON h_st.hobby_id = hobby.hobby_id
                        WHERE h_st.student_id = ? AND hobby.topic = ?
                        """,
                        tableName
                ),
                Integer.class,
                studentId, topic
        );
        return count != null && count > 0;
    }

    public List<HobbyModel> save(Long hobbyId, Long studentId) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (hobby_id, student_id)" +
                                "VALUES (?, ?)",
                        tableName
                ),
                hobbyId,
                studentId
        );

        return findByStudent(studentId);
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
                    var hobbyStudentRelations = new HobbyStudentRelations(rs.getLong("hobby_id"),
                            rs.getLong("student_id"));
                    return hobbyStudentRelations;
                },
                hobbyStudentId
        );
    }
}