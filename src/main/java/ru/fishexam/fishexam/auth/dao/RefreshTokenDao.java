package ru.fishexam.fishexam.auth.dao;

import java.sql.Timestamp;
import java.util.Optional;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.auth.models.RefreshToken;

public class RefreshTokenDao {

    private final PostgreSqlJdbcTemplate mainDb;
    private static final String tableName = "user_refresh_tokens";

    public RefreshTokenDao(PostgreSqlJdbcTemplate mainDb) {
        this.mainDb = mainDb;
    }

    public Optional<RefreshToken> findByToken(String token) {
         return mainDb.queryForObjectOptional(
                String.format(
                        """
                        SELECT * from %s
                        WHERE refresh_token = ?
                        """,
                        tableName
                ),
                (rs, num) -> new RefreshToken(
                        rs.getString("username"),
                        token,
                        rs.getTimestamp("expiry_date").toInstant()
                ),
                token
        );
    }
    public void deleteByUser(String username) {
        mainDb.update(
                String.format(
                        """
                        DELETE FROM %s
                        WHERE username = ?
                        """,
                        tableName
                ),
                username
        );
    }

    public void save(RefreshToken refreshToken) {
        mainDb.update(
                String.format(
                        """
                        INSERT INTO %s (refresh_token, username, expiry_date)
                        VALUES (?, ?, ?)
                        ON CONFLICT (refresh_token) DO UPDATE
                        SET
                            username = EXCLUDED.username,
                            expiry_date = EXCLUDED.expiry_date
                        """,
                        tableName
                ),
                refreshToken.getToken(),
                refreshToken.getUsername(),
                Timestamp.from(refreshToken.getExpiryDate())
        );
    }
}
