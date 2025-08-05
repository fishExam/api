package ru.fishexam.fishexam.auth.dao;

import java.util.Optional;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.auth.models.UserAuth;
import ru.fishexam.fishexam.dto.user.UserRole;

public class UserDao {

  private final PostgreSqlJdbcTemplate mainDb;
  private final String tableName = "users";

  public UserDao(PostgreSqlJdbcTemplate mainDb) {
    this.mainDb = mainDb;
  }

  public Optional<UserAuth> findByUsername(String username) {
    return mainDb.queryForObjectOptional(
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
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("user_role").toUpperCase())
            ),
            username
    );
  }

  public Optional<UserAuth> findById(Long id) {
    return mainDb.queryForObjectOptional(
            String.format(
                    """
                    SELECT * from %s
                    WHERE user_id = ?
                    """,
                    tableName
            ),
            (rs, num) -> new UserAuth(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("user_role").toUpperCase())
            ),
            id
    );
  }

  public Boolean existsByUsername(String username) {
    var count = mainDb.queryForObject(
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

  public void save(Long id, String username, String password, UserRole userRole) {
    mainDb.update(
            String.format(
                    "INSERT INTO %s (user_id, username, password, user_role)" +
                            "VALUES (?, ?, ?, ?)",
                    tableName
            ),
            id,
            username,
            password,
            userRole.name().toLowerCase()
    );
  }

  public void update(UserAuth userAuth) {
    mainDb.update(
            String.format(
                    """
                        INSERT INTO %s (user_id, username, password, user_role)
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (user_id) DO UPDATE SET
                        username = EXCLUDED.username,
                        password = EXCLUDED.password,
                        user_role = EXCLUDED.user_role
                    """,
                    tableName
            ),
            userAuth.getUserId(),
            userAuth.getUsername(),
            userAuth.getPassword(),
            userAuth.getUserRole().name().toLowerCase()
    );
  }
}
