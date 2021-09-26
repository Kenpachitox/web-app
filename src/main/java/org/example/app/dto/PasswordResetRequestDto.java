package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasswordResetRequestDto extends RegistrationRequestDto {
    private String username;
    private String password;
    private String secretCode;
}
