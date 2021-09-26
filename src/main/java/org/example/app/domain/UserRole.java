package org.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@AllArgsConstructor
@Data
public class UserRole {
  private long userId;
  private String role;
}
