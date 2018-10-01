package com.github.gvacaliuc.solitaire.model;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.Deque;
import java.util.Optional;
import org.junit.Test;

public class TableauCardPileTest {

  public static final Deque<Card> QUEEN_STRAIGHT_RED =
      Queues.newArrayDeque(
          Lists.newArrayList(
              Card.of(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
              Card.of(Card.Suit.CLUBS, Card.Rank.JACK),
              Card.of(Card.Suit.DIAMONDS, Card.Rank.TEN),
              Card.of(Card.Suit.CLUBS, Card.Rank.NINE),
              Card.of(Card.Suit.DIAMONDS, Card.Rank.EIGHT)));

  public static final Deque<Card> KING_STRAIGHT_BLACK =
      Queues.newArrayDeque(
          Lists.newArrayList(
              Card.of(Card.Suit.SPADES, Card.Rank.KING),
              Card.of(Card.Suit.HEARTS, Card.Rank.QUEEN),
              Card.of(Card.Suit.SPADES, Card.Rank.JACK),
              Card.of(Card.Suit.HEARTS, Card.Rank.TEN),
              Card.of(Card.Suit.SPADES, Card.Rank.NINE)));

  @Test
  public void display() {}

  @Test
  public void pushPile() {
    // Make sure we don't allow an empty push
    final Deque<Card> emptyDeque = Queues.newArrayDeque();
    CardPile kingPile = new TableauCardPile(KING_STRAIGHT_BLACK, 1);
    assertFalse(kingPile.pushPile(emptyDeque).isPresent());

    // Make sure we don't allow anything but a king onto the empty pile
    CardPile emptyPile = new TableauCardPile(emptyDeque, 0);
    assertFalse(emptyPile.pushPile(QUEEN_STRAIGHT_RED).isPresent());
    assertTrue(emptyPile.pushPile(KING_STRAIGHT_BLACK).isPresent());

    // Make sure we can push a queen onto a king
    CardPile justKing =
        new TableauCardPile(
            Queues.newArrayDeque(Lists.newArrayList(KING_STRAIGHT_BLACK.getFirst())), 1);
    assertTrue(justKing.pushPile(QUEEN_STRAIGHT_RED).isPresent());

    // Make sure we can't push a king onto an eight
    CardPile queenPile = new TableauCardPile(QUEEN_STRAIGHT_RED, 1);
    assertFalse(queenPile.pushPile(KING_STRAIGHT_BLACK).isPresent());
  }

  @Test
  public void peek() {
    // Make sure we can't pop more than we can see
    CardPile kingPile = new TableauCardPile(KING_STRAIGHT_BLACK, 1);
    assertFalse(kingPile.peek(2).isPresent());

    // Make sure we can pop a card we can see!
    Optional<Deque<Card>> optionalPile = kingPile.peek(1);
    assertTrue(optionalPile.isPresent());
    assertArrayEquals(
        new Card[] {Card.of(Card.Suit.SPADES, Card.Rank.NINE)}, optionalPile.get().toArray());

    // And another if we remove that one!
    optionalPile = kingPile.remove(1).get().peek(1);
    assertTrue(optionalPile.isPresent());
    assertArrayEquals(
        new Card[] {Card.of(Card.Suit.HEARTS, Card.Rank.TEN)}, optionalPile.get().toArray());
  }

  @Test
  public void remove() {
    // Make sure we can't remove more than we can see
    CardPile kingPile = new TableauCardPile(KING_STRAIGHT_BLACK, 1);
    assertFalse(kingPile.remove(2).isPresent());

    // Make sure we can remove a card we can see!
    Optional<CardPile> optionalPile = kingPile.remove(1);
    Deque<Card> kingStraightLessOne = Queues.newArrayDeque(KING_STRAIGHT_BLACK);
    kingStraightLessOne.removeLast();
    assertTrue(optionalPile.isPresent());
    assertArrayEquals(kingStraightLessOne.toArray(), optionalPile.get().getDeque().toArray());

    // And another if we remove that one!
    optionalPile = optionalPile.get().remove(1);
    kingStraightLessOne.removeLast();
    assertTrue(optionalPile.isPresent());
    assertArrayEquals(kingStraightLessOne.toArray(), optionalPile.get().getDeque().toArray());
  }
}
