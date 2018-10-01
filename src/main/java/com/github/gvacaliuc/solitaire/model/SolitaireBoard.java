package com.github.gvacaliuc.solitaire.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SolitaireBoard {

  private Deck deck;
  private Foundation foundation;
  private Tableu tableu;

  public SolitaireBoard() throws Exception {
    foundation = Foundation.makeEmpty();
    tableu = new Tableu();
    deck = tableu.init(Deck.freshDeck());
  }

  private SolitaireBoard(Deck deck, Foundation foundation, Tableu tableu) {
    this.deck = deck;
    this.foundation = foundation;
    this.tableu = tableu;
  }

  /**
   * Writes the state of this board to a string.
   *
   * @return
   */
  public String toString() {
    return List.of(deck.toString(), foundation.toString(), tableu.toString())
        .stream()
        .collect(Collectors.joining("\n===\n"));
  }

  /** Displays the current state of the solitaire board. */
  public void display() {
    System.out.println(toString());
  }

  public boolean isFinished() {
    boolean finished = true;
    for (Card.Suit suit : Card.Suit.values()) {
      finished &=
          foundation.peekCard(suit).map(card -> card.getRank() == Card.Rank.KING).orElse(false);
    }
    return finished;
  }

  public Optional<SolitaireBoard> advanceTalon() {
    Optional<Card> oCard = deck.peek();

    if (!oCard.isPresent()) return Optional.empty();

    final Card card = oCard.get();

    return deck.push(card).map(newDeck -> new SolitaireBoard(newDeck, foundation, tableu));
  }

  public Optional<SolitaireBoard> talonToTableu(int destPile) {

    Optional<Card> oCard = deck.peek();

    if (!oCard.isPresent()) return Optional.empty();

    final Card card = oCard.get();

    return tableu
        .pushCards(destPile, Queues.newArrayDeque(Lists.newArrayList(card)))
        .map(newTableu -> new SolitaireBoard(deck.remove().get(), foundation, newTableu));
  }

  public Optional<SolitaireBoard> talonToFoundation() {
    Optional<Card> oCard = deck.peek();

    if (!oCard.isPresent()) return Optional.empty();

    final Card card = oCard.get();

    return foundation
        .pushCard(card)
        .map(newFoundation -> new SolitaireBoard(deck.remove().get(), newFoundation, tableu));
  }

  public Optional<SolitaireBoard> tableuToTableu(int originPile, int destPile) {

    // dumb impl - just try to remove all from numVisible down
    for (int numCards = tableu.getNumVisible(originPile); numCards > 0; numCards--) {
      Optional<Deque<Card>> cards = tableu.peekPile(originPile, numCards);
      if (cards.isPresent()) {
        final int numCardsFinal = numCards;
        Optional<Tableu> newTableu =
            tableu
                .pushCards(destPile, cards.get())
                .map(largeTableu -> largeTableu.pullCards(originPile, numCardsFinal).get());
        if (newTableu.isPresent())
          return Optional.of(new SolitaireBoard(deck, foundation, newTableu.get()));
      }
    }

    return Optional.empty();
  }

  public Optional<SolitaireBoard> tableuToFoundation(int originPile) {
    Optional<Deque<Card>> oCards = tableu.peekPile(originPile, 1);

    if (!oCards.isPresent()) return Optional.empty();

    final Card card = oCards.get().peek();

    return foundation
        .pushCard(card)
        .map(
            newFoundation ->
                new SolitaireBoard(deck, newFoundation, tableu.pullCards(originPile, 1).get()));
  }

  public Optional<SolitaireBoard> restartGame() throws Exception {
    return Optional.of(new SolitaireBoard());
  }
}
