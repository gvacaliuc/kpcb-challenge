package com.github.gvacaliuc.solitaire.view;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Scanner;
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

  public void start() {
    Scanner inputScanner = new Scanner(System.in);

    while (true) {
      System.out.println(adapter.getBoardAsString());
      final String inputLine = inputScanner.nextLine();
      final ActionResult result = executeCommand(inputLine);
      System.out.println(result);
    }
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

    return actionMap
        .getOrDefault(leftToken, ImmutableMap.of())
        .getOrDefault(rightToken, (a, b) -> failureFunc.get())
        .apply(left, right);
  }
}
