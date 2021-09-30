package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.*;
import org.example.app.dto.*;
import org.example.app.exception.*;
import org.example.app.repository.RoleRepository;
import org.example.app.repository.TokenRepository;
import org.example.app.repository.UserRepository;
import org.example.app.util.UsernameChecker;
import org.example.framework.security.*;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.*;

@RequiredArgsConstructor
public class UserService implements AuthenticationProvider, AnonymousProvider {
  private final UserRepository repository;
  private final RoleRepository roleRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final StringKeyGenerator keyGenerator;

  @Override
  public Authentication authenticate(Authentication authentication) {
    try {
      final var token = (String) authentication.getPrincipal();
      final var userId = tokenRepository.getTokenWithTime(token).map(Token::getUserId).orElseThrow(AuthenticationException::new);
      final var auth = new TokenAuthentication(repository.getByUserId(userId), null,List.of(),true);

      if (auth.isAuthenticated()) {
        return auth;
      }else {
        return provide();
      }
    } catch (RuntimeException e){
      throw new AuthenticationException(e);
    }

  }

  @Override
  public AnonymousAuthentication provide() {
    return new AnonymousAuthentication(new AnonUser());
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
    final var saved = repository.save(0, username, hash).orElseThrow(RegistrationException::new);
    final var role = roleRepository.save(0,List.of(Roles.ROLE_USER), saved.getId());
    Set<String> roleList = new HashSet<>();
    for (UserRole r:role) {
      roleList.add(r.getRole());
    }
    tokenRepository.saveToken(saved.getId(), token);
    return new RegistrationResponseDto(saved.getId(), saved.getUsername(), token, roleList);
  }

  public LoginResponseDto login(LoginRequestDto requestDto) {
    final var username = requestDto.getUsername().trim().toLowerCase();
    final var password = requestDto.getPassword().trim();

    // FIXME: Security issue
    final var saved = repository.getByUsernameWithPassword(username).orElseThrow(UserNotFoundException::new);
    final var role = roleRepository.getRoleByUserId(saved.getId());
    Set<String> roleList = new HashSet<>();
    for (UserRole r:role) {
      roleList.add(r.getRole());
    }
    if (!passwordEncoder.matches(password, saved.getPassword())) {
      // FIXME: Security issue
      throw new PasswordNotMatchesException();
    }

    final boolean tokenIsDropped = dropToken(saved,15);

    final var token = keyGenerator.generateKey();
    tokenRepository.saveToken(saved.getId(), token);
    return new LoginResponseDto(saved.getId(), saved.getUsername(), token, roleList);
  }

  private boolean dropToken(UserWithPassword userWithPassword, long minuts) {
    final var tokenToDropWithTime = tokenRepository.getTokenWithTimeByUserId(userWithPassword.getId());
    final var tokenCreateTime = tokenToDropWithTime
            .map(Token::getTokenCreateTime)
            .orElseThrow(LoginException::new);
    final var tokenToDrop = tokenToDropWithTime
            .map(Token::getToken)
            .orElseThrow(RuntimeException::new);
    final var needToDropToken = (OffsetDateTime.parse(tokenCreateTime).plusMinutes(minuts)).compareTo(OffsetDateTime.now());
    if (needToDropToken <= 0){
      tokenRepository.dropToken(tokenToDrop);
      return true;
    }
    return false;
  }


  public void generateSecretCode(String userName){
    var code= new int[6];
    for (int i = 0; i < code.length; i++) {
      code[i] = new SecureRandom().nextInt(9);
    }
    final var user = repository.getByUsername(userName).orElseThrow(UsernameNotFoundException::new);
    repository.saveSecretCode(Arrays.toString(code), user.getId());
  }

  public RegistrationResponseDto resetPassword(PasswordResetRequestDto requestDto) {
    final var user = repository.getByUsername(requestDto.getUsername())
            .orElseThrow(AuthenticationException::new);
    final var secretCode = repository.getSecretCode(user.getId()).orElseThrow(IllegalArgumentException::new);
    if (!secretCode.getSecretCode().equals(requestDto.getSecretCode())) {
      throw new IncorrectSecretCode("Try again later");
    }
    repository.dropSecretCode(user.getId());
    return register(requestDto);

  }
}
