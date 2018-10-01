package com.github.gvacaliuc.solitaire.model;

public class Card {

  public enum Suit {
    DIAMONDS,
    CLUBS,
    HEARTS,
    SPADES;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public enum Rank {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    @Override
    public String toString() {
      return (ordinal() + 1 <= 10 && ordinal() + 1 > 1)
          ? String.valueOf(ordinal() + 1)
          : name().toLowerCase();
    }
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  private final Suit suit;
  private final Rank rank;

  private Card(final Suit suit, final Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public static Card of(final Suit suit, final Rank rank) {
    return new Card(suit, rank);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card)) return false;

    final Card other = (Card) obj;
    return other.getRank() == getRank() && other.getSuit() == getSuit();
  }

  @Override
  public String toString() {
    return String.format("[%s]: %s", suit.toString(), rank.toString());
  }

  public String display(boolean visible) {
    return visible ? toString() : "CARD";
  }
}
