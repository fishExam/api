package ru.fishexam.fishexam.auth.dao;

import java.time.LocalDate;
import java.util.Optional;

import ru.fishexam.fishexam.dao.PostgreSqlJdbcTemplate;
import ru.fishexam.fishexam.auth.models.UserAuth;

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
                    WHERE surname = ?
                    """,
                    tableName
            ),
            (rs, num) -> new UserAuth(
                    rs.getLong("user_id"),
                    rs.getString("surname"),
                    rs.getString("first_name"),
                    rs.getString("patronymic"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getDate("birth").toLocalDate(),
                    rs.getString("telegram_id"),
                    rs.getString("password")
            ),
            username
    );
  }

  public Boolean existsByUsername(String username) {
    var count = mainDb.queryForObject(
            String.format(
                    """
                            SELECT COUNT(*) FROM %s where surname = ?
                    """,
                    tableName
            ),
            Integer.class,
            username
    );

    return count != null && count > 0;
  }

  public UserAuth save(String username, String first_name, String patronymic, String phone,
                       String email, LocalDate birth, String telegram_id, String password) {
    mainDb.update(
            String.format(
                    "INSERT INTO %s (surname, first_name, patronymic, phone, email, birth, telegram_id, password)" +
                            "VALUES (?, ?, ?, ?, ?, ?::date, ?, ?)",
                    tableName
            ),
            username,
            first_name,
            patronymic,
            phone,
            email,
            birth,
            telegram_id,
            password
    );

    return findByUsername(username).orElseThrow();
  }

  public void update(UserAuth userAuth) {
    mainDb.update(
            String.format(
                    """
                        INSERT INTO %s (user_id, surname, first_name, patronymic, phone, email,
                        birth, telegram_id, password)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON CONFLICT (user_id) DO UPDATE SET
                        surname = EXCLUDED.surname,
                        first_name = EXCLUDED.first_name,
                        patronymic = EXCLUDED.patronymic,
                        phone = EXCLUDED.phone,
                        email = EXCLUDED.email,
                        birth = EXCLUDED.birth,
                        telegram_id = EXCLUDED.telegram_id,
                        password = EXCLUDED.password
                    """,
                    tableName
            ),
            userAuth.getUserid(),
            userAuth.getSurname(),
            userAuth.getFirst_name(),
            userAuth.getPatronymic(),
            userAuth.getPhone(),
            userAuth.getEmail(),
            userAuth.getBirth(),
            userAuth.getTelegram_id(),
            userAuth.getPassword()
    );
  }
}
