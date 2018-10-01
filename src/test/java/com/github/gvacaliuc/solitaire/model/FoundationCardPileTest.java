package com.github.gvacaliuc.solitaire.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.Deque;
import java.util.Optional;
import org.junit.Test;

public class FoundationCardPileTest {

  @Test
  public void pushPile() {
    // Make sure we don't allow anything but an ace onto the empty pile
    CardPile clubPile = FoundationCardPile.makeEmpty(Card.Suit.CLUBS);
    assertFalse(clubPile.pushPile(queueOf(Card.of(Card.Suit.CLUBS, Card.Rank.TWO))).isPresent());
    Optional<CardPile> oAcePile =
        clubPile.pushPile(queueOf(Card.of(Card.Suit.CLUBS, Card.Rank.ACE)));
    assertTrue(oAcePile.isPresent());

    // Make sure we can push a two onto an ace
    Optional<CardPile> oTwoPile =
        oAcePile.get().pushPile(queueOf(Card.of(Card.Suit.CLUBS, Card.Rank.TWO)));
    assertTrue(oTwoPile.isPresent());

    // Make sure we can't push a five onto a two
    assertFalse(
        oTwoPile.get().pushPile(queueOf(Card.of(Card.Suit.CLUBS, Card.Rank.FIVE))).isPresent());
  }

  @Test
  public void peek() {
    // Make sure we don't allow an empty peek
    CardPile clubPile = FoundationCardPile.makeEmpty(Card.Suit.CLUBS);
    assertFalse(clubPile.peek(1).isPresent());

    // Make sure we don't allow a peek of more than 1 card
    Optional<CardPile> oAcePile =
        clubPile.pushPile(queueOf(Card.of(Card.Suit.CLUBS, Card.Rank.ACE)));
    assertTrue(oAcePile.isPresent());
    Optional<CardPile> oTwoPile =
        oAcePile.get().pushPile(queueOf(Card.of(Card.Suit.CLUBS, Card.Rank.TWO)));
    assertTrue(oTwoPile.isPresent());
    assertFalse(oTwoPile.get().peek(2).isPresent());
    assertTrue(oTwoPile.get().peek(1).isPresent());
  }

  @Test
  public void remove() {
    // This returns Optional.empty() for now.
  }

  private static <T> Deque<T> queueOf(T... cards) {
    return Queues.newArrayDeque(Lists.newArrayList(cards));
  }
}
