package org.example.app.handler;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.app.domain.Card;
import org.example.app.domain.User;
import org.example.app.exception.DatabaseException;
import org.example.app.exception.NotEnoughRightsException;
import org.example.app.service.CardService;
import org.example.app.util.CardHelper;
import org.example.app.util.UserHelper;
import org.example.framework.security.Roles;

import java.io.IOException;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public class CardHandler { // Servlet -> Controller -> Service (domain) -> domain
  private final CardService service;
  private final Gson gson;

  public void getAll(HttpServletRequest req, HttpServletResponse resp) {
    try {
      // cards.getAll?ownerId=1
      final var user = UserHelper.getUser(req);
      final var data = service.getAllByOwnerId(user.getId());
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(gson.toJson(data));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void getById(HttpServletRequest req, HttpServletResponse resp) {
    final var cardId = CardHelper.getCardId(req);
    if (checkAccess(req)){
      log.log(Level.INFO, "getById");
    }
  }

  public void order(HttpServletRequest req, HttpServletResponse resp) {
    final var cardId = CardHelper.getCardId(req);
    if (checkAccess(req)){
      log.log(Level.INFO, "card ordered");
    }
  }

  public void blockById(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var cardId = CardHelper.getCardId(req);
      if (checkAccess(req))  {
        final var data = service.blockById(cardId);
        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().write(gson.toJson(data));
      } else throw new NotEnoughRightsException();
    }
    catch (IOException e) {
        throw new RuntimeException(e);
    }
  }

  public boolean checkAccess(HttpServletRequest req){
    final var cardId = CardHelper.getCardId(req);
    final var user = UserHelper.getUser(req);
    final var roles = UserHelper.getRoles(req);
    final var ownerId = service.getById(cardId)
            .map(Card::getOwnerId)
            .orElseThrow(DatabaseException::new);
    return roles.contains(Roles.ROLE_ADMIN) || user.getId() == ownerId;

  }
}
