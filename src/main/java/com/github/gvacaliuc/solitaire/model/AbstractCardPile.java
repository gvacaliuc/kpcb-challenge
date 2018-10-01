package com.github.gvacaliuc.solitaire.model;

import com.google.common.collect.Queues;
import java.util.Deque;
import java.util.Optional;

public abstract class AbstractCardPile implements CardPile {

  protected Deque<Card> cardStack;
  protected int numVisible;

  /**
   * Determines whether two cards are compatible to be stacked. Bottom is
   *
   * @param base the card to be stacked on
   * @param newCard the card to add
   * @return true if newCard can be stacked on base
   */
  protected abstract boolean compatible(Card base, Card newCard);

  /**
   * Determines whether a card can start a pile.
   *
   * @param starter the card we wish to start a pile with
   * @return true if card can start a pile
   */
  protected abstract boolean validPileStarter(Card starter);

  protected abstract CardPile addPile(Deque<Card> newCards);

  protected abstract CardPile newPile(Deque<Card> newCards);

  @Override
  public Optional<CardPile> pushPile(Deque<Card> newCards) {

    // Don't deal with this case
    if (newCards.isEmpty()) return Optional.empty();

    // If we're empty, we only accept a king
    if (cardStack.isEmpty()) {
      if (validPileStarter(newCards.getFirst())) {
        return Optional.of(newPile(newCards));
      }
      return Optional.empty();
    }

    if (compatible(cardStack.getLast(), newCards.getFirst())) {
      return Optional.of(addPile(newCards));
    }

    return Optional.empty();
  }

  @Override
  public Deque<Card> getDeque() {
    return Queues.newArrayDeque(cardStack);
  }
}
