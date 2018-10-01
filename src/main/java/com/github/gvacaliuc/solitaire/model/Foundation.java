package com.github.gvacaliuc.solitaire.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.*;
import java.util.stream.Collectors;

public class Foundation {
  private Map<Card.Suit, CardPile> foundations = new HashMap<>(4);

  public static Foundation makeEmpty() {
    return new Foundation(
        ImmutableMap.<Card.Suit, CardPile>builder()
            .put(Card.Suit.CLUBS, FoundationCardPile.makeEmpty(Card.Suit.CLUBS))
            .put(Card.Suit.DIAMONDS, FoundationCardPile.makeEmpty(Card.Suit.DIAMONDS))
            .put(Card.Suit.SPADES, FoundationCardPile.makeEmpty(Card.Suit.SPADES))
            .put(Card.Suit.HEARTS, FoundationCardPile.makeEmpty(Card.Suit.HEARTS))
            .build());
  }

  private Foundation(Map<Card.Suit, CardPile> foundations) {
    this.foundations = foundations;
  }

  public Optional<Foundation> pushCard(Card card) {

    final CardPile smallPile = foundations.get(card.getSuit());

    final Optional<CardPile> newPile =
        smallPile.pushPile(Queues.newArrayDeque(Lists.newArrayList(card)));

    return newPile.map(
        largePile ->
            new Foundation(
                ImmutableMap.<Card.Suit, CardPile>builder()
                    .putAll(Maps.filterKeys(foundations, key -> key != card.getSuit()))
                    .put(card.getSuit(), largePile)
                    .build()));
  }

  public Optional<Card> peekCard(Card.Suit suit) {
    return foundations.get(suit).peek(1).map(Deque::peek);
  }

  public Optional<Foundation> pullCard(Card.Suit suit) {
    return foundations
        .get(suit)
        .remove(1)
        .map(
            smallPile ->
                new Foundation(
                    ImmutableMap.<Card.Suit, CardPile>builder()
                        .putAll(Maps.filterKeys(foundations, key -> key != suit))
                        .put(suit, smallPile)
                        .build()));
  }

  @Override
  public String toString() {
    return Arrays.stream(Card.Suit.values())
        .map(suit -> String.format("%s: %s", suit.name(), foundations.get(suit).toString()))
        .collect(Collectors.joining("\n"));
  }
}
