package ru.fishexam.fishexam.dao;

import ru.fishexam.fishexam.dto.OutlineCreate;

import java.util.Optional;

public class OutlineCreateDao {
    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "outline";

    public OutlineCreateDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public void update(OutlineCreate outlineCreate) {
        mainDb.update(
                String.format(
                        """
                                INSERT INTO %s (author_id, title)
                                VALUES (?, ?)
                                ON CONFLICT (outline_id) DO UPDATE SET
                                    author_id = EXCLUDED.author_id,
                                    title = EXCLUDED.title
                                """,
                        tableName
                ),
                outlineCreate.getAuthorId(),
                outlineCreate.getTitle()
        );
    }

    public Optional<OutlineCreate> getById(Long outlineId) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                                SELECT * from %s
                                WHERE outline_id = ?
                                """,
                        tableName
                ),
                (rs, num) -> {
                    var outlineCreate = new OutlineCreate();
                    outlineCreate.setOutlineId(outlineId);
                    outlineCreate.setAuthorId(rs.getLong("author_id"));
                    outlineCreate.setTitle(rs.getString("title"));
                    return outlineCreate;
                },
                outlineId
        );
    }
}