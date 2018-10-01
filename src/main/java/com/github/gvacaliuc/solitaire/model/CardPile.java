package com.github.gvacaliuc.solitaire.model;

import java.util.Deque;
import java.util.Optional;

/** Interface to interact with */
public interface CardPile {
  /**
   * Adds a whole card pile to this card pile. If it's accepted, a new card pile is returned.
   *
   * @param cardPile the cardPile to add to the pile
   * @return a new card pile wrapped in an optional if the card is accepted, else an empty optional.
   */
  Optional<CardPile> pushPile(Deque<Card> cardPile);

  /**
   * Removes a pile of size numCards from this pile.
   *
   * @param numCards the number of cards to remove
   * @return a card pile less the cards removed
   */
  Optional<CardPile> remove(int numCards);

  /**
   * Returns the last n cards on this card pile.
   *
   * @param numCards the number of cards to pop off
   * @return an optional containing the cards if we can successfully pop these off, else empty
   */
  Optional<Deque<Card>> peek(int numCards);

  Deque<Card> getDeque();

  int getNumVisible();
}
