package ru.fishexam.fishexam.dao.outline;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.dto.outline.OutlineCreate;

import java.util.Optional;

public class OutlineCreateDao {
    private final PostgreSqlJdbcTemplate mainDb;
    private final String tableName = "outline";

    public OutlineCreateDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<OutlineCreate> findByAuthorIdAndTitle(Long authId, String title) {
        return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE author_id = ? AND title = ?
                        """,
                        tableName
                ),
                (rs, num) -> new OutlineCreate(
                        rs.getLong("outline_id"),
                        rs.getLong("author_id"),
                        rs.getString("title")
                ),
                authId, title
        );
    }

    public Boolean existsByIdAndTitle(Long authId, String title) {
        var count = mainDb.queryForObject(
                String.format(
                        """
                        SELECT COUNT(*) from %s
                        WHERE author_id = ? AND title = ?
                        """,
                        tableName
                ),
                Integer.class,
                authId, title
        );
        return count != null && count > 0;
    }

    public Optional<OutlineCreate> save(Long authorId, String title) {
        mainDb.update(
                String.format(
                        "INSERT INTO %s (author_id, title)" +
                                "VALUES (?, ?)",
                        tableName
                ),
                authorId, title
        );

        return findByAuthorIdAndTitle(authorId, title);
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
                    var outlineCreate = new OutlineCreate(outlineId,
                            rs.getLong("author_id"),
                            rs.getString("title"));
                    return outlineCreate;
                },
                outlineId
        );
    }
}