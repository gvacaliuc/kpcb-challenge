package com.github.gvacaliuc.solitaire.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.*;

public class Deck {

  private Deque<Card> cards;

  private Deck(Deque<Card> cards) {
    this.cards = cards;
  }

  public static Deck freshDeck() {
    Card.Suit[] suits = Card.Suit.values();
    Card.Rank[] ranks = Card.Rank.values();
    Deque<Card> cards = new ArrayDeque<>(suits.length * ranks.length);
    for (Card.Suit suit : suits) {
      for (Card.Rank rank : ranks) {
        cards.addLast(Card.of(suit, rank));
      }
    }
    return shuffle(new Deck(cards));
  }

  public Deque<Card> getCards() {
    return cards;
  }

  /** Shuffles the deck. */
  public static Deck shuffle(Deck deck) {
    List<Card> tmpList = Lists.newArrayList(deck.getCards());
    Collections.shuffle(tmpList);
    return new Deck(Queues.newArrayDeque(tmpList));
  }

  /**
   * Draws a card from this deck.
   *
   * @return the first card in this deck
   */
  public Optional<Card> peek() {
    return cards.isEmpty() ? Optional.empty() : Optional.of(cards.peekFirst());
  }

  public Optional<Deck> remove() {
    Deque<Card> newCards = Queues.newArrayDeque(cards);
    newCards.removeFirst();
    return Optional.of(new Deck(newCards));
  }

  /**
   * Puts a card back on the Deck - returns a new object.
   *
   * @param card the card to add
   * @return a new Deck with the card pushed into the underlying queue
   */
  public Optional<Deck> push(Card card) {
    Deque<Card> newCards = Queues.newArrayDeque(cards);
    newCards.removeFirst();
    newCards.addLast(card);
    return Optional.of(new Deck(newCards));
  }

  /** Class to throw when the deck is empty, or would be after an operation. */
  public static class EmptyDeckException extends Exception {
    public EmptyDeckException(String errorMessage) {
      super(errorMessage);
    }
  }

  @Override
  public String toString() {
    final Card top = cards.peekFirst();
    if (top != null) return String.format("DECK: %s", cards.peekFirst().toString());
    else return "EMPTY DECK";
  }
}
