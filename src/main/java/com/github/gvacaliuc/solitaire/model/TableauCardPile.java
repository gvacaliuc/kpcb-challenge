package com.github.gvacaliuc.solitaire.model;

import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;

/** Represents a card pile in the Tableau. */
public class TableauCardPile extends AbstractCardPile {

  public TableauCardPile(Deque<Card> cards, int numVisible) {
    this.cardStack = Queues.newArrayDeque(cards);
    this.numVisible = numVisible;
  }

  public static TableauCardPile of(int numVisible, Card... cards) {
    return new TableauCardPile(Queues.newArrayDeque(Arrays.asList(cards)), numVisible);
  }

  @Override
  protected boolean validPileStarter(Card starter) {
    return starter.getRank() == Card.Rank.KING;
  }

  @Override
  protected boolean compatible(Card top, Card bottom) {
    return (top.getRank().ordinal() - 1 == bottom.getRank().ordinal())
        && (top.getSuit().ordinal() % 2 != bottom.getSuit().ordinal() % 2);
  }

  @Override
  protected CardPile newPile(Deque<Card> newCards) {
    return new TableauCardPile(newCards, newCards.size());
  }

  @Override
  protected CardPile addPile(Deque<Card> newCards) {
    int visible = numVisible + newCards.size();
    Deque<Card> newDeque = Queues.newArrayDeque(cardStack);
    for (Card c : newCards) {
      newDeque.addLast(c);
    }
    return new TableauCardPile(newDeque, visible);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("[ ");

    final int size = cardStack.size();
    int count = 0;

    for (Card c : cardStack) {
      stringBuilder.append(c.display(count >= (size - numVisible)));
      stringBuilder.append(", ");
      count++;
    }

    stringBuilder.append(" ]");

    return stringBuilder.toString();
  }

  @Override
  public Optional<Deque<Card>> peek(int numCards) {
    if (numVisible < numCards || cardStack.size() < numCards) return Optional.empty();

    Deque<Card> copyStack = Queues.newArrayDeque(cardStack);
    Deque<Card> retStack = Queues.newArrayDeque();

    int count = 0;
    while (count < numCards) {
      retStack.addFirst(copyStack.removeLast());
      count++;
    }

    return Optional.of(retStack);
  }

  @Override
  public Optional<CardPile> remove(int numCards) {
    if (numVisible < numCards || cardStack.size() < numCards) return Optional.empty();

    int copyNumVisible = numVisible;
    Deque<Card> copyStack = Queues.newArrayDeque(cardStack);
    Deque<Card> retStack = Queues.newArrayDeque();

    int count = 0;
    while (count < numCards) {
      retStack.addFirst(copyStack.removeLast());
      copyNumVisible--;
      count++;
    }

    copyNumVisible = Math.max(copyNumVisible, 1);

    return Optional.of(new TableauCardPile(copyStack, copyNumVisible));
  }

  @Override
  public int getNumVisible() {
    return numVisible;
  }
}
