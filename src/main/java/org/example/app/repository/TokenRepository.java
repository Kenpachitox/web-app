package org.example.app.repository;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.Token;
import org.example.app.domain.User;
import org.example.app.domain.UserRole;
import org.example.jdbc.JdbcTemplate;
import org.example.jdbc.RowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenRepository {
  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Token> rowMapper = resultSet -> new Token(
          resultSet.getString("token"),
          resultSet.getString("created"),
          resultSet.getLong("userId")
  );

  public Optional<Token> getTokenWithTime(String token) {
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
            """
              SELECT token, "userId", created FROM tokens WHERE token = ?;
              """,
            rowMapper,
            token,token
    );
  }

  public Optional<Token> getTokenWithTimeByUserId(long userId) {
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
            """
              SELECT token, "userId", created FROM tokens WHERE "userId" = ?
              """,
            rowMapper,
            userId
    );
  }


  public void saveToken(long userId, String token) {
    // query - SELECT'ов (ResultSet)
    // update - ? int/long
    // language=PostgreSQL
    jdbcTemplate.update(
            """
                INSERT INTO tokens(token, "userId") VALUES (?, ?)
                """,
            token, userId
    );
  }

  public void dropToken(String token) {
    // language=PostgreSQL
    jdbcTemplate.update(
            """
                DELETE FROM tokens WHERE token = ?
                """,
            token
    );
  }
}
