package com.github.gvacaliuc.solitaire.game;

import com.github.gvacaliuc.solitaire.model.SolitaireBoard;
import com.github.gvacaliuc.solitaire.view.IViewToModelAdapter;
import com.github.gvacaliuc.solitaire.view.SolitaireTUI;

import java.util.Optional;

public class GameController {

  public enum GameResult {
    SUCCESS,
    FAILURE;

    String message = name();

    public GameResult setMessage(String message) {
      this.message = message;
      return this;
    }

    @Override
    public String toString() {
      return message;
    }
  }

  private SolitaireTUI<GameResult> view;
  private SolitaireBoard board;

  public GameController() throws Exception {
    init();
  }

  public void start() {
    view.start();
  }

  private void init() throws Exception {
    board = new SolitaireBoard();

    view =
        new SolitaireTUI<>(
            new IViewToModelAdapter<GameResult>() {
              @Override
              public GameResult advanceTalon() {
                Optional<SolitaireBoard> oBoard = board.advanceTalon();
                if (!oBoard.isPresent()) return GameResult.FAILURE;
                board = oBoard.get();
                return GameResult.SUCCESS;
              }

              @Override
              public GameResult talonToTableu(int destPile) {
                System.err.println("talon to tableu");
                Optional<SolitaireBoard> oBoard = board.talonToTableu(destPile);
                if (!oBoard.isPresent()) return GameResult.FAILURE;
                board = oBoard.get();
                return GameResult.SUCCESS;
              }

              @Override
              public GameResult talonToFoundation() {
                System.err.println("talon to foundation called");
                Optional<SolitaireBoard> oBoard = board.talonToFoundation();
                if (!oBoard.isPresent()) return GameResult.FAILURE;
                board = oBoard.get();
                return GameResult.SUCCESS;
              }

              @Override
              public GameResult tableuToTableu(int originPile, int destPile) {
                System.err.println("tableu to tableu called");
                Optional<SolitaireBoard> oBoard = board.tableuToTableu(originPile, destPile);
                if (!oBoard.isPresent()) return GameResult.FAILURE;
                board = oBoard.get();
                return GameResult.SUCCESS;
              }

              @Override
              public GameResult tableuToFoundation(int originPile) {
                System.err.println("tableu to foundation called");
                Optional<SolitaireBoard> oBoard = board.tableuToFoundation(originPile);
                if (!oBoard.isPresent()) return GameResult.FAILURE;
                board = oBoard.get();
                return GameResult.SUCCESS;
              }

              @Override
              public GameResult restartGame() {
                try {
                  board = new SolitaireBoard();
                  return GameResult.SUCCESS.setMessage("Successfully restarted Solitaire!");
                } catch (Exception e) {
                  e.printStackTrace();
                  return GameResult.FAILURE;
                }
              }

              @Override
              public GameResult getFailure() {
                return GameResult.FAILURE;
              }

              @Override
              public String getBoardAsString() {
                return board.toString();
              }
            });
  }

  public static void main(String[] args) throws Exception {
    GameController controller = new GameController();
    controller.start();
  }
}
