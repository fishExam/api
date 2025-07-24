package ru.fishexam.fishexam.dao.hobby;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.hobby.HobbyModel;

import java.util.List;
import java.util.Optional;

public class HobbyModelDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "hobby";

    public HobbyModelDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<HobbyModel> findHobby(String topic) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE topic = ?
                        """,
                        tableName
                ),
                (rs, num) -> new HobbyModel(
                        rs.getLong("hobby_id"),
                        rs.getString("topic")
                ),
                topic
        );
    }

    public HobbyModel save(String topic) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (topic)" +
                                "VALUES (?)",
                        tableName
                ),
                topic
        );
        return findHobby(topic).orElseThrow();
    }

    public void update(HobbyModel hobbyModel) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (topic)
                            VALUES (?)
                            ON CONFLICT (hobby_id) DO UPDATE SET
                            topic = EXCLUDED.topic
                        """,
                        tableName
                ),
                hobbyModel.getTopic()
        );
    }

    public Optional<HobbyModel> getById(Long hobbyId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE hobby_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var hobbyModel = new HobbyModel(
                            rs.getLong("hobby_id"),
                            rs.getString("topic"));
                    return hobbyModel;
                },
                hobbyId
        );
    }

    public List<String> getStudentHobby(Long studentId) {
        return mainDb.query(
                String.format(
                        """
                                SELECT topic from %s hs
                                JOIN hobby_student h ON h.hobby_id = hs.hobby_id
                                WHERE h.student_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> rs.getString("topic") ,
                studentId
        );
    }

    public Long getId(String topic) {
        return mainDb.queryForObject(
                String.format(
                        """
                                SELECT hobby_id from %s 
                                WHERE topic = ?
                                """,
                        tableName
                ),
                (rs, num) -> rs.getLong("hobby_id"),
                topic
        );
    }
}