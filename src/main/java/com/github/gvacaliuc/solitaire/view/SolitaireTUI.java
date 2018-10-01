package com.github.gvacaliuc.solitaire.view;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class SolitaireTUI<ActionResult> {

  private IViewToModelAdapter<ActionResult> adapter;

  private enum SolitaireToken {
    TALON("(t|talon)"),
    TABLEAU("[0-6]"),
    FOUNDATION("(f|foundation)"),
    RESTART("(r|restart)"),
    FAIL("");

    private final String regex;

    SolitaireToken(String regex) {
      this.regex = regex;
    }

    static SolitaireToken match(String input) {
      SolitaireToken inputMatch = FAIL;
      boolean matched = false;
      for (SolitaireToken token : SolitaireToken.values()) {
        if (Pattern.matches(token.regex, input))
          if (matched) return SolitaireToken.FAIL;
          else {
            matched = true;
            inputMatch = token;
          }
      }
      return inputMatch;
    }
  }

  static class Pair<T, U> {

    public final T left;
    public final U right;

    private Pair(final T left, final U right) {
      this.left = left;
      this.right = right;
    }

    public static <T, U> Pair<T, U> of(T left, U right) {
      return new Pair<T, U>(left, right);
    }

    @Override
    public boolean equals(Object obj) {

      System.err.println(String.format("Equals called on: %s, %s", toString(), obj.toString()));

      if (!(obj instanceof Pair)) return false;

      Pair pair = (Pair) obj;

      return pair.left == left && pair.right == right;
    }

    @Override
    public String toString() {
      return String.format("Pair(%s, %s)", left.toString(), right.toString());
    }
  }

  private final Map<SolitaireToken, Map<SolitaireToken, BiFunction<String, String, ActionResult>>>
      actionMap =
          ImmutableMap.of(
              SolitaireToken.TALON,
                  ImmutableMap.of(
                      SolitaireToken.FOUNDATION,
                      (t, f) -> adapter.talonToFoundation(),
                      SolitaireToken.TABLEAU,
                      (t, pile) -> adapter.talonToTableu(Integer.valueOf(pile))),
              SolitaireToken.TABLEAU,
                  ImmutableMap.of(
                      SolitaireToken.TABLEAU,
                      (pile1, pile2) ->
                          adapter.tableuToTableu(Integer.valueOf(pile1), Integer.valueOf(pile2)),
                      SolitaireToken.FOUNDATION,
                      (pile, f) -> adapter.tableuToFoundation(Integer.valueOf(pile))));

  private final Supplier<ActionResult> failureFunc = () -> adapter.getFailure();

  public SolitaireTUI(IViewToModelAdapter<ActionResult> adapter) {
    this.adapter = adapter;
  }

  public ActionResult executeCommand(String inputLine) {
    if (inputLine.trim().isEmpty()) return adapter.advanceTalon();

    if (SolitaireToken.match(inputLine) == SolitaireToken.RESTART) return adapter.restartGame();

    String[] splitString = inputLine.toLowerCase().trim().split("->");

    if (splitString.length != 2) {
      return failureFunc.get();
    }

    final String left = splitString[0].trim();
    final String right = splitString[1].trim();

    final SolitaireToken leftToken = SolitaireToken.match(left);
    final SolitaireToken rightToken = SolitaireToken.match(right);

    System.err.println(String.format("parsed tokens: 1(%s), 2(%s)", left, right));

    return actionMap
        .getOrDefault(leftToken, ImmutableMap.of())
        .getOrDefault(rightToken, (a, b) -> failureFunc.get())
        .apply(left, right);
  }
}
