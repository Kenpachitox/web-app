package org.example.app.service;

import lombok.RequiredArgsConstructor;
import org.example.app.domain.Card;
import org.example.app.repository.CardRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CardService {
  private final CardRepository cardRepository;

  public List<Card> getAllByOwnerId(long ownerId) {
    return cardRepository.getAllByOwnerId(ownerId);
  }

  public int blockById(long cardId){
    return cardRepository.blockById(cardId);
  }

  public Optional<Card> getByCardNumber(String cardNumber){
    return cardRepository.getByCardNumber(cardNumber);
  }

  public Optional<Card> getById(long cardId){
    return cardRepository.getById(cardId);
  }

  public Optional<Card> order(long id, long ownerId, String number, long balance, boolean active) {
    return cardRepository.order(id,ownerId,number,balance,active);
  }
}
