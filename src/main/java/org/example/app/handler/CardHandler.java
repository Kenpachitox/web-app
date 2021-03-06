package org.example.app.handler;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.app.domain.Card;
import org.example.app.dto.TransferDto;
import org.example.app.exception.*;
import org.example.app.service.CardService;
import org.example.app.util.CardHelper;
import org.example.app.util.UserHelper;
import org.example.framework.security.Roles;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public class CardHandler { // Servlet -> Controller -> Service (domain) -> domain
  private final CardService service;
  private final Gson gson;

  public void getAll(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var user = UserHelper.getUser(req);
      final var data = service.getAllByOwnerId(user.getId(),user);
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(data));
    } catch (IOException e) {
      throw new CardNotFoundException();
    }
  }

  public void getById(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var cardId = CardHelper.getCardId(req);
      final var user = UserHelper.getUser(req);
      final var data = service.getById(cardId,user);
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(data));
    } catch (IOException e) {
      throw new CardNotFoundException();
    }
  }

  public void order(HttpServletRequest req, HttpServletResponse resp) {
    final var user = UserHelper.getUser(req);
    log.log(Level.INFO,"Card ordered");
  }

  public void blockById(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var cardId = CardHelper.getCardId(req);
      final var user = UserHelper.getUser(req);
      final var data = service.blockById(cardId, user);
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(data));
    }
    catch (IOException e) {
        throw new CardBlockException();
    }
  }



  public void transferMoney(HttpServletRequest req, HttpServletResponse resp){
    try {
      final var TransferDto = gson.fromJson(req.getReader(), TransferDto.class);
      final var user = UserHelper.getUser(req);
      service.transferMoney(TransferDto.getSenderCardId(),user, TransferDto.getRecipientCardId(),TransferDto.getAmount());
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(TransferDto));
    } catch (IOException e) {
        throw new TransferException();
    }
  }
}
