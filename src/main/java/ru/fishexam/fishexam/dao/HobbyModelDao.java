package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.HobbyModel;

import java.util.List;
import java.util.Optional;

public class HobbyModelDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "hobby";

    public HobbyModelDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
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
                    var hobbyModel = new HobbyModel();
                    hobbyModel.setHobbyId(hobbyId);
                    hobbyModel.setTopic(rs.getString("topic"));
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
                                JOIN hobby h ON h.hobby_id = hs.hobby_id
                                WHERE hs.student_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> rs.getString("topic") ,
                studentId
        );
    }
    public Optional<HobbyModel> createIfNot(String hobby) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT topic, hobby_id from %s 
                                WHERE topic = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var hobbyModel = new HobbyModel();
                    hobbyModel.setHobbyId(rs.getLong("hobby_id"));
                    hobbyModel.setTopic(rs.getString("topic"));
                    return hobbyModel;
                },
                hobby
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