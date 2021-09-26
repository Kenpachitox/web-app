package org.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.framework.security.Roles;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class AnonUser {
  private long id = -1;
  private String username = "anonymous";
  private Collection<String> role = List.of(Roles.ROLE_ANONYMOUS);
}
