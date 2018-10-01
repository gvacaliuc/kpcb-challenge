package com.github.gvacaliuc.solitaire.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.Optional;

public class FoundationCardPile extends AbstractCardPile {

  private final Card.Suit suit;

  private FoundationCardPile(Card.Suit suit, Deque<Card> cards) {
    this(suit, cards, 1);
  }

  public static FoundationCardPile makeEmpty(Card.Suit suit) {
    return new FoundationCardPile(suit, Queues.newArrayDeque());
  }

  private FoundationCardPile(Card.Suit suit, Deque<Card> cards, int numVisible) {
    this.suit = suit;
    this.cardStack = cards;
    this.numVisible = numVisible;
  }

  @Override
  protected boolean validPileStarter(Card starter) {
    return starter.getRank() == Card.Rank.ACE && starter.getSuit() == suit;
  }

  @Override
  protected boolean compatible(Card base, Card newCard) {
    return base.getRank().ordinal() + 1 == newCard.getRank().ordinal()
        && newCard.getSuit() == suit
        && newCard.getSuit() == base.getSuit();
  }

  @Override
  protected CardPile newPile(Deque<Card> newCards) {
    return new FoundationCardPile(suit, newCards);
  }

  @Override
  protected CardPile addPile(Deque<Card> newCards) {
    Deque<Card> newDeque = Queues.newArrayDeque(cardStack);
    for (Card c : newCards) {
      newDeque.addLast(c);
    }
    return new FoundationCardPile(suit, newDeque);
  }

  @Override
  public Optional<CardPile> pushPile(Deque<Card> newCards) {
    // Only support pushing a single card at a time
    if (newCards.size() > 1) return Optional.empty();

    return super.pushPile(newCards);
  }

  @Override
  public Optional<CardPile> remove(int numCards) {
    return Optional.empty();
  }

  @Override
  public Optional<Deque<Card>> peek(int numCards) {
    return numCards != 1
        ? Optional.empty()
        : cardStack.isEmpty()
            ? Optional.empty()
            : Optional.of(Queues.newArrayDeque(Lists.newArrayList(cardStack.peekLast())));
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("[ ");

    stringBuilder.append(suit.name());
    stringBuilder.append(" - ");
    stringBuilder.append(cardStack.isEmpty() ? "empty" : cardStack.peekLast().display(true));

    stringBuilder.append(" ]");

    return stringBuilder.toString();
  }

  @Override
  public int getNumVisible() {
    return numVisible;
  }
}
