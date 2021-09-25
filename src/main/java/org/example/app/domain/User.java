package org.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.framework.security.Authentication;

import java.util.Collection;

@AllArgsConstructor
@Data
public class User {
  private long id;
  private String username;
  private Collection<String> role;
}
