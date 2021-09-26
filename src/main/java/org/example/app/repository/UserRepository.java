package org.example.app.repository;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.SecretCode;
import org.example.app.domain.UserRole;
import org.example.app.domain.User;
import org.example.app.domain.UserWithPassword;
import org.example.jdbc.JdbcTemplate;
import org.example.jdbc.RowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<User> rowMapper = resultSet -> new User(
      resultSet.getLong("id"),
      resultSet.getString("username")
  );
  private final RowMapper<UserWithPassword> rowMapperWithPassword = resultSet -> new UserWithPassword(
      resultSet.getLong("id"),
      resultSet.getString("username"),
      resultSet.getString("password")
  );
  private final RowMapper<SecretCode> rowMapperForSecretCode = resultSet -> new SecretCode(
          resultSet.getString("code"),
          resultSet.getLong("userId")
  );

  public Optional<User> getByUsername(String username) {
    // language=PostgreSQL
    return jdbcTemplate.queryOne("SELECT id, username FROM users WHERE username = ?", rowMapper, username);
  }

  public Optional<UserWithPassword> getByUsernameWithPassword(String username) {
    // language=PostgreSQL
    return jdbcTemplate.queryOne("SELECT id, username, password FROM users WHERE username = ?", rowMapperWithPassword, username);
  }

  /**
   * saves user to db
   *
   * @param id - user id, if 0 - insert, if not 0 - update
   * @param username
   * @param hash
   */
  // TODO: DuplicateKeyException <-
  public Optional<User> save(long id, String username, String hash) {
    // language=PostgreSQL
    return id == 0 ? jdbcTemplate.queryOne(
        """
            INSERT INTO users(username, password) VALUES (?, ?) RETURNING id, username;
            """,
        rowMapper,
        username, hash
    ) : jdbcTemplate.queryOne(
        """
            UPDATE users SET username = ?, password = ? WHERE id = ? RETURNING id, username;
            """,
        rowMapper,
        username, hash, id
    );
  }

  public Optional<User> getByUserId(long id) {
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
            """
                SELECT id, username FROM users  WHERE id = ?
                """,
            rowMapper,
            id
    );
  }


  public void saveSecretCode(String code, long userId){
    // language=PostgreSQL
    jdbcTemplate.update(
        """
            INSERT INTO secret_codes (code, "userId") VALUES (?, ?)
            """,
        rowMapperForSecretCode,
        code,
        userId
    );
  }

  public Optional<SecretCode> getSecretCode(long userId){
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
      """
          SELECT code, "userId"  FROM secret_codes WHERE "userId" = ?
          """,
        rowMapperForSecretCode,
        userId
    );
  }

  public Optional<SecretCode> dropSecretCode(long userId){
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
            """
                DELETE FROM secret_codes WHERE "userId" = ?
                """,
            rowMapperForSecretCode,
            userId
    );
  }

}
