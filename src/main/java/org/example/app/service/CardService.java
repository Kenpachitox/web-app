package org.example.app.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.app.domain.Card;
import org.example.app.domain.User;
import org.example.app.domain.UserRole;
import org.example.app.exception.*;
import org.example.app.repository.CardRepository;
import org.example.app.repository.RoleRepository;
import org.example.app.util.CardHelper;
import org.example.app.util.UserHelper;
import org.example.framework.security.Roles;

import javax.management.relation.Role;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class CardService {
  private final CardRepository cardRepository;
  private final RoleRepository roleRepository;

  public List<Card> getAllByOwnerId(long ownerId,User user) {
    if (checkAdminRole(user) ){
      return cardRepository.getAllByOwnerId(ownerId);
    } else {
      throw new NotEnoughRightsException();
    }
  }

  public int blockById(long cardId, User user){

    if (checkAdminRole(user) || checkOwner(cardId,user)){
      return cardRepository.blockById(cardId);
    } else {
      throw new NotEnoughRightsException();
    }

  }

  public Optional<Card> getByCardNumber(String cardNumber){
    return cardRepository.getByCardNumber(cardNumber);
  }

  public Optional<Card> getById(long cardId, User user){
    if (checkAdminRole(user) || checkOwner(cardId,user)){
      return cardRepository.getById(cardId);
    }else {
      throw new NotEnoughRightsException();
    }
  }

  public Optional<Card> order(long id,long ownerId,String number,long balance,boolean active) {
      return cardRepository.order(id,ownerId,number,balance,active);
  }

  public Optional<Card> transferMoney(long senderCardId, User user, long recipientCardId, long amount){
    if (checkOwner(senderCardId, user)) {
      final var balance = cardRepository.getById(senderCardId)
              .map(Card::getBalance)
              .orElseThrow(DatabaseException::new);
      if (amount > balance) {
        throw new NotEnoughFundsException();
      }
      if (amount <= 0) {
        throw new IncorrectAmountException();
      }
      return cardRepository.transferMoney(senderCardId, recipientCardId, amount);
    } else{
      throw new NotEnoughRightsException();
    }
  }

  public boolean checkOwner(long cardId, User user){
    final var ownerId = cardRepository.getById(cardId)
            .map(Card::getOwnerId)
            .orElseThrow(DatabaseException::new);
    return user.getId() == ownerId;
  }

  public boolean checkAdminRole(User user){
    final var roleList = roleRepository.getRoleByUserId(user.getId());
    Set<String> roleSet = new HashSet<>();
    for (UserRole r:roleList) {
      roleSet.add(r.getRole());
    }
    return roleSet.contains(Roles.ROLE_ADMIN);
  }
}
