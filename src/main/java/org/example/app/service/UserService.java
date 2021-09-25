package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.User;
import org.example.app.dto.LoginRequestDto;
import org.example.app.dto.LoginResponseDto;
import org.example.app.dto.RegistrationRequestDto;
import org.example.app.dto.RegistrationResponseDto;
import org.example.app.exception.PasswordNotMatchesException;
import org.example.app.exception.RegistrationException;
import org.example.app.exception.UserNotFoundException;
import org.example.app.repository.UserRepository;
import org.example.app.util.UsernameChecker;
import org.example.framework.security.*;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
public class UserService implements AuthenticationProvider, AnonymousProvider {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final StringKeyGenerator keyGenerator;

  @Override
  public Authentication authenticate(Authentication authentication) {
    final var token = (String) authentication.getPrincipal();

    return repository.findByToken(token)
        // TODO: add user roles
        .map(o -> new TokenAuthentication(o, null, List.of(), true))
        .orElseThrow(AuthenticationException::new);
  }

  @Override
  public AnonymousAuthentication provide() {
    return new AnonymousAuthentication(new User(
        -1,
        "anonymous",
            List.of(Roles.ROLE_ANONYMOUS)
    ));
  }

  public RegistrationResponseDto register(RegistrationRequestDto requestDto) {
    // TODO login:
    //  case-sensitivity: coursar Coursar
    //  cleaning: "  Coursar   "
    //  allowed symbols: [A-Za-z0-9]{2,60}
    //  mis...: Admin, Support, root, ...
    //  мат: ...
    //  check for nullability
    final var username = requestDto.getUsername().trim().toLowerCase();
    if (requestDto.getUsername() == null) {
      throw new RegistrationException("username can't be empty");
    }

    if (!username.matches("^[A-Za-z0-9]{2,60}$")) {
      throw new RegistrationException("username does not meet requirements");
    }

    if (!UsernameChecker.checkValidUsername(username)){
      throw new RegistrationException("obscene language and service names cannot be used as username");
    }
    // TODO password:
    //  min-length: 8
    //  max-length: 64
    //  non-dictionary
    final var password = requestDto.getPassword().trim();

    if (requestDto.getPassword() == null) {
      throw new RegistrationException("password can't be null");
    }

    if (requestDto.getPassword().length() < 8 || requestDto.getPassword().length() > 64) {
      throw new RegistrationException("password must be bigger than 8 and less than 64");
    }

    //FIXME: do not throw the password through the classes
    if (!UsernameChecker.checkValidPassword(password)){
      throw new RegistrationException("password is too easy");
    }

    final var hash = passwordEncoder.encode(password);
    final var token = keyGenerator.generateKey();
    final var saved = repository.save(0, username, hash,List.of(Roles.ROLE_USER)).orElseThrow(RegistrationException::new);

    repository.saveToken(saved.getId(), token);
    return new RegistrationResponseDto(saved.getId(), saved.getUsername(), token);
  }

  public LoginResponseDto login(LoginRequestDto requestDto) {
    final var username = requestDto.getUsername().trim().toLowerCase();
    final var password = requestDto.getPassword().trim();

    // FIXME: Security issue
    final var saved = repository.getByUsernameWithPassword(username).orElseThrow(UserNotFoundException::new);

    if (!passwordEncoder.matches(password, saved.getPassword())) {
      // FIXME: Security issue
      throw new PasswordNotMatchesException();
    }

    final var token = keyGenerator.generateKey();
    repository.saveToken(saved.getId(), token);
    return new LoginResponseDto(saved.getId(), saved.getUsername(), token);
  }
}
