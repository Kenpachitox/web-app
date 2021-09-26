package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDto {
  private long id;
  private String username;
  private String token;
  private Collection<String> role;
}
