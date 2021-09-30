package org.example.app.repository;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.Card;
import org.example.jdbc.JdbcTemplate;
import org.example.jdbc.RowMapper;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CardRepository {
  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Card> cardRowMapper = resultSet -> new Card(
      resultSet.getLong("id"),
      resultSet.getString("number"),
      resultSet.getLong("balance")
  );
  private final RowMapper<Card> cardFullRowMapper = resultSet -> new Card(
          resultSet.getLong("id"),
          resultSet.getString("number"),
          resultSet.getLong("balance"),
          resultSet.getLong("ownerId")

  );

  public List<Card> getAllByOwnerId(long ownerId) {
    // language=PostgreSQL
    return jdbcTemplate.queryAll(
        "SELECT id, number, balance FROM cards WHERE \"ownerId\" = ? AND active = TRUE",
        cardRowMapper,
        ownerId
    );
  }

  public int blockById(long cardId) {
    // language=PostgreSQL
    return jdbcTemplate.update(
            """
            UPDATE cards SET active = FALSE WHERE id = ?;
            """,
            cardRowMapper,
            cardId
    );
  }

  public Optional<Card> getById(long cardId){
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
            """
            SELECT id, number,balance,"ownerId" FROM cards WHERE id = ?;
                """,
            cardFullRowMapper,
            cardId
    );

  }

  public Optional<Card> getByCardNumber(String cardNumber){
    // language=PostgreSQL
    return jdbcTemplate.queryOne(
            """
              SELECT id, number,balance,"ownerId" from cards where number = ?;
                """
                ,
            cardFullRowMapper,
            cardNumber
    );
  }

  public Optional<Card> order(long id, long ownerId, String number, long balance, boolean active) {
    // language=PostgreSQL
    return id == 0 ? jdbcTemplate.queryOne(
            """
                INSERT INTO cards("ownerId", number, balance, active) VALUES (?, ?, ?, ?) RETURNING id, "ownerId", number, balance, active;
                """,
            cardFullRowMapper,
            ownerId, number, balance, active
    ) : jdbcTemplate.queryOne(
            """
                UPDATE cards SET "ownerId" = ?, number = ?, balance = ?, active = ? WHERE id = ? RETURNING id, "ownerId", number, balance, active;
                """,
            cardFullRowMapper,
            ownerId, number, balance, active, id
    );
  }

  public Optional<Card> transferMoney(long senderCardId, long recipientCardId, long amount){
    // language=PostgreSQL
     var senderCard = jdbcTemplate.queryOne(
            """
              UPDATE cards SET balance = balance - ? WHERE id = ? AND active = TRUE RETURNING id, number, balance;
                """
            ,
            cardRowMapper,
            amount, senderCardId
    );
    // language=PostgreSQL
    jdbcTemplate.queryOne(
            """
              UPDATE cards SET balance = balance + ? WHERE id = ? AND active = TRUE;
                """
            ,
            cardRowMapper,
             amount, recipientCardId
    );
    return senderCard;
  }
}
