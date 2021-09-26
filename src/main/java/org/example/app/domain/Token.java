package org.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Token {
  private String token;
  private String tokenCreateTime;
  private long userId;
}
