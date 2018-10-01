package com.github.gvacaliuc.solitaire.view;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public interface IViewToModelAdapter<ActionResult> {
  ActionResult advanceTalon();

  ActionResult talonToTableu(int destPile);

  ActionResult talonToFoundation();

  ActionResult tableuToTableu(int originPile, int destPile);

  ActionResult tableuToFoundation(int originPile);

  ActionResult restartGame();

  ActionResult getFailure();
}
