package org.example.app.handler;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.app.domain.User;
import org.example.app.dto.LoginRequestDto;
import org.example.app.dto.PasswordResetRequestDto;
import org.example.app.dto.RegistrationRequestDto;
import org.example.app.exception.LoginException;
import org.example.app.exception.NotCreateSecretCodeException;
import org.example.app.exception.PasswordResetException;
import org.example.app.exception.RegistrationException;
import org.example.app.service.CardService;
import org.example.app.service.UserService;
import org.example.app.util.UserHelper;
import org.example.framework.attribute.RequestAttributes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;

@Log
@RequiredArgsConstructor
public class UserHandler {
  private final UserService service;
  private final Gson gson;

  public void register(HttpServletRequest req, HttpServletResponse resp) {
    try {
      log.log(Level.INFO, "register");
      final var requestDto = gson.fromJson(req.getReader(), RegistrationRequestDto.class);
      final var responseDto = service.register(requestDto);
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(responseDto));
    } catch (IOException e) {
      throw new RegistrationException();
    }
  }

  public void login(HttpServletRequest req, HttpServletResponse resp) {
    try {
      log.log(Level.INFO, "login");
      final var requestDto = gson.fromJson(req.getReader(), LoginRequestDto.class);
      final var responseDto = service.login(requestDto);
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(responseDto));
    } catch (IOException e) {
      throw new LoginException();
    }
  }

  public void generateSecretCode(HttpServletRequest req, HttpServletResponse resp) {
    try {
      log.log(Level.INFO, "generateSecretCode");
      final var user = UserHelper.getUser(req);
      final var secretCode = service.generateSecretCode(user.getUsername());
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(secretCode));
    } catch (IOException e){
      throw new NotCreateSecretCodeException();
    }

  }

  public void passwordReset(HttpServletRequest req, HttpServletResponse resp) {
    try {
      log.log(Level.INFO, "passwordReset");
      final var passwordResetRequestDto = gson.fromJson(req.getReader(), PasswordResetRequestDto.class);
      final var passwordResetResponseDto = service.resetPassword(passwordResetRequestDto);
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(passwordResetResponseDto));
    }catch (IOException e){
      throw new PasswordResetException();
    }
  }
}
