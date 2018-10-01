package com.github.gvacaliuc.solitaire.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TableuTest {

  public static final Tableu table1 =
      Tableu.of(
          TableuCardPile.of(
              5,
              Card.of(Card.Suit.CLUBS, Card.Rank.KING),
              Card.of(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
              Card.of(Card.Suit.CLUBS, Card.Rank.JACK),
              Card.of(Card.Suit.DIAMONDS, Card.Rank.TEN),
              Card.of(Card.Suit.CLUBS, Card.Rank.NINE)),
          TableuCardPile.of(0),
          TableuCardPile.of(
              4,
              Card.of(Card.Suit.HEARTS, Card.Rank.TEN),
              Card.of(Card.Suit.CLUBS, Card.Rank.NINE),
              Card.of(Card.Suit.HEARTS, Card.Rank.EIGHT),
              Card.of(Card.Suit.CLUBS, Card.Rank.SEVEN),
              Card.of(Card.Suit.HEARTS, Card.Rank.SIX)),
          TableuCardPile.of(
              2,
              Card.of(Card.Suit.SPADES, Card.Rank.EIGHT),
              Card.of(Card.Suit.DIAMONDS, Card.Rank.SEVEN),
              Card.of(Card.Suit.CLUBS, Card.Rank.SIX)));

  @Test
  public void testMoveToEmptyPile() {
    // make sure we can't move a non king to an empty pile
    assertFalse(table1.moveCards(2, 1, 3).isPresent());

    // make sure we can move a king to an empty pile
    assertTrue(table1.moveCards(0, 1, 5).isPresent());
  }

  @Test
  public void moveTest() throws Exception {
    // check we can move cards successfully
    assertTrue(table1.moveCards(2, 0, 3).isPresent());
    assertTrue(table1.pullCards(0, 1).get().moveCards(2, 0, 4).isPresent());

    // check we can't pair up spades & clubs
    assertFalse(table1.moveCards(3, 0, 3).isPresent());

    // check we can't pair up hearts & diamonds
    assertFalse(table1.pullCards(2, 2).get().moveCards(3, 2, 2).isPresent());
  }

  @Test
  public void testInit() throws Exception {
    System.out.println(new Tableu(Deck.freshDeck()).toString());
  }
}
