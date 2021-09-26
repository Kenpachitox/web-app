package org.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SecretCode {
    private String secretCode;
    private long userId;
}
