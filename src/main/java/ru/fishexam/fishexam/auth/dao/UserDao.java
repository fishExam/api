package ru.fishexam.fishexam.auth.dao;

import java.util.Optional;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.auth.models.UserAuth;

public class UserDao {

  private final PostgreSqlJdbcTemplate postgreSqlJdbcTemplate;
  private final String tableName = "users";

  public UserDao(PostgreSqlJdbcTemplate postgreSqlJdbcTemplate) {
    this.postgreSqlJdbcTemplate = postgreSqlJdbcTemplate;
  }

  public Optional<UserAuth> findByUsername(String username) {
    return postgreSqlJdbcTemplate.queryForObjectOptional(
            String.format(
                    """
                    SELECT * from %s
                    WHERE username = ?
                    """,
                    tableName
            ),
            (rs, num) -> new UserAuth(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password")
            ),
            username
    );
  }

  public Boolean existsByUsername(String username) {
    var count = postgreSqlJdbcTemplate.queryForObject(
            String.format(
                    """
                            SELECT COUNT(*) FROM %s where username = ?
                    """,
                    tableName
            ),
            Integer.class,
            username
    );

    return count != null && count > 0;
  }

  public UserAuth save(String username, String password) {
    postgreSqlJdbcTemplate.update(
            String.format(
                    "INSERT INTO %s (username, password) VALUES (?, ?)",
                    tableName
            ),
            username,
            password
    );

    return findByUsername(username).orElseThrow();
  }
}
