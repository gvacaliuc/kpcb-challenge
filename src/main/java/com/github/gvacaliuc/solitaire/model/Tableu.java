package com.github.gvacaliuc.solitaire.model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tableu {

  private static final int NUMBER_OF_TABLEU_PILES = 7;

  private CardPile[] piles = new TableuCardPile[NUMBER_OF_TABLEU_PILES];

  private Tableu(CardPile[] piles) {
    this.piles = piles;
  }

  public Tableu() {}

  public static Tableu of(CardPile... cardPiles) {
    return new Tableu(cardPiles);
  }

  @SuppressWarnings("unchecked")
  public Deck init(Deck deck) throws Deck.EmptyDeckException {

    Deque<Card>[] deques = new Deque[NUMBER_OF_TABLEU_PILES];

    // Initialize the array.
    for (int i = 0; i < NUMBER_OF_TABLEU_PILES; i++) {
      deques[i] = new ArrayDeque<>(i + 1);
    }

    // Per solitaire rules, fill across all piles before placing a second card
    for (int card = 0; card < NUMBER_OF_TABLEU_PILES; card++) {
      for (int pile = card; pile < NUMBER_OF_TABLEU_PILES; pile++) {
        final Card cardToAdd =
            deck.peek()
                .orElseThrow(
                    () -> new Deck.EmptyDeckException("Empty deck when creating tableu..."));
        deck = deck.remove().orElseThrow();
        deques[card].addLast(cardToAdd);
      }
    }

    int pileId = 0;
    for (Deque<Card> deque : deques) {
      piles[pileId] = new TableuCardPile(deque, 1);
      pileId++;
    }

    return deck;
  }

  public Optional<Tableu> moveCards(int originPile, int destPile, int numCards) {
    // Can we remove these cards?
    Optional<Deque<Card>> migrants = piles[originPile].peek(numCards);
    if (!migrants.isPresent()) return Optional.empty();

    // Can we push these cards?
    Optional<CardPile> oNewPile = piles[destPile].pushPile(migrants.get());
    if (!oNewPile.isPresent()) return Optional.empty();

    // Remove from origin.
    Optional<CardPile> originRemoved = piles[originPile].remove(numCards);
    if (!originRemoved.isPresent()) return Optional.empty();

    // Create new pile for the new Tableu
    final CardPile[] newPiles = Arrays.copyOf(piles, piles.length);
    newPiles[originPile] = originRemoved.get();
    newPiles[destPile] = oNewPile.get();

    return Optional.of(new Tableu(newPiles));
  }

  public Optional<Tableu> pushCards(int pile, Deque<Card> cards) {
    return piles[pile]
        .pushPile(cards)
        .map(
            newPile -> {
              CardPile[] pileCopy = Arrays.copyOf(piles, piles.length);
              pileCopy[pile] = newPile;
              return new Tableu(pileCopy);
            });
  }

  public Optional<Deque<Card>> peekPile(int pile, int numCards) {
    return piles[pile].peek(numCards);
  }

  public Optional<Tableu> pullCards(int pile, int numCards) {
    return piles[pile]
        .remove(numCards)
        .map(
            newPile -> {
              CardPile[] pileCopy = Arrays.copyOf(piles, piles.length);
              pileCopy[pile] = newPile;
              return new Tableu(pileCopy);
            });
  }

  @Override
  public String toString() {
    return IntStream.range(0, piles.length)
        .mapToObj(i -> String.format("Pile %d: %s", i, piles[i].toString()))
        .collect(Collectors.joining("\n"));
  }

  public int getNumVisible(int pile) {
    return piles[pile].getNumVisible();
  }
}
