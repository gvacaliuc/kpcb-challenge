package com.github.gvacaliuc.solitaire.view;

public interface IViewToModelAdapter<ActionResult> {
  ActionResult advanceTalon();

  ActionResult talonToTableu(int destPile);

  ActionResult talonToFoundation();

  ActionResult tableuToTableu(int originPile, int destPile);

  ActionResult tableuToFoundation(int originPile);

  ActionResult restartGame();

  ActionResult getFailure();

  String getBoardAsString();
}
