package ru.fishexam.fishexam.dao.outline;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.outline.OutlineCreate;
import ru.fishexam.fishexam.dto.outline.OutlineStudentRelation;

import java.util.Optional;

public class OutlineStudentRelationsDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "outline_student";

    public OutlineStudentRelationsDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<OutlineCreate> findOutlineByStudent(Long studentId, Long outlineId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from outline
                        JOIN %s o_st ON o_st.outline_id = outline.outline_id
                        WHERE o_st.student_id = ? AND o_st = ?
                        """,
                        tableName
                ),
                (rs, num) -> new OutlineCreate(
                        rs.getLong("outline_id"),
                        rs.getLong("student_id"),
                        rs.getString("title")
                ),
                studentId, outlineId
        );
    }

    public Boolean existsByOutlineStudent(Long studentId, Long outlineId) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(*) from %s
                        WHERE student_id = ? AND outline_id = ?
                        """,
                        tableName
                ),
                Integer.class,
                studentId, outlineId
        );
        return count != null && count > 0;
    }

    public OutlineCreate save(Long studentId, Long outlineId) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (outline_id, student_id)" +
                                "VALUES (?, ?)",
                        tableName
                ),
                outlineId,
                studentId
        );

        return findOutlineByStudent(studentId, outlineId).orElseThrow();
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