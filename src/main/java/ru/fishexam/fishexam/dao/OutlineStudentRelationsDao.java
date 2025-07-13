package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.OutlineStudentRelation;

import java.util.Optional;

public class OutlineStudentRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "outline_student";

    public OutlineStudentRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(OutlineStudentRelation outlineStudentRelation) {
        mainDb.update(
                String.format(
                        """
                            INSERT INTO %s (outline_id, student_id)
                            VALUES (?, ?)
                            ON CONFLICT (outline_student_id) DO UPDATE SET
                            outline_id = EXCLUDED.outline_id,
                            student_id = EXCLUDED.student_id
                        """,
                        tableName
                ),
                outlineStudentRelation.getOutlineId(),
                outlineStudentRelation.getStudentId()
        );
    }

    public Optional<OutlineStudentRelation> getById(Long outlineStudentId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE outline_student_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var outlineStudentRelation = new OutlineStudentRelation();
                    outlineStudentRelation.setOutlineStudentId(outlineStudentId);
                    outlineStudentRelation.setOutlineId(rs.getLong("outline_id"));
                    outlineStudentRelation.setStudentId(rs.getLong("student_id"));
                    return outlineStudentRelation;
                },
                outlineStudentId
        );
    }
}