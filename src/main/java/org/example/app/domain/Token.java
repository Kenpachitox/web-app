package org.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Data
public class Token {
  private String token;
  private Timestamp tokenCreateTime;
  private Timestamp tokenUsedTime;
  private long userId;
}
