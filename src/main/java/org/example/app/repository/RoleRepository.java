package org.example.app.repository;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.User;
import org.example.app.domain.UserRole;
import org.example.app.domain.UserWithPassword;
import org.example.jdbc.JdbcTemplate;
import org.example.jdbc.RowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RoleRepository {
  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<UserRole> rowMapperForRole = resultSet -> new UserRole(
          resultSet.getLong("userId"),
          resultSet.getString("role")
  );

  public List<UserRole> getRoleByUserId(long userId) {
    // language=PostgreSQL
    return jdbcTemplate.queryAll(
            """
              SELECT role, "userId" FROM roles WHERE "userId" = ?
              """,
            rowMapperForRole,
            userId
    );
  }

  // TODO: DuplicateKeyException <-
  public List<UserRole> save(long id, Collection<String> role, long userId) {
    // language=PostgreSQL
    return id == 0 ? jdbcTemplate.queryAll(
        """
            INSERT INTO roles(role,"userId") VALUES (?,?) RETURNING role, "userId"
            """,
            rowMapperForRole,
        role,userId
    ) : jdbcTemplate.queryAll(
        """
            UPDATE roles SET role = ? WHERE "userId" = ? RETURNING role, "userId"
            """,
            rowMapperForRole,
         role, userId
    );
  }
}
