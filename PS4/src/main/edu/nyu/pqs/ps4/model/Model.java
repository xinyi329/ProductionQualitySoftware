package edu.nyu.pqs.ps4.model;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import edu.nyu.pqs.ps4.model.player.Player;
import edu.nyu.pqs.ps4.model.player.PlayerFactory;
import edu.nyu.pqs.ps4.model.player.PlayerType;
import edu.nyu.pqs.ps4.view.Listener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the game model of Connect Four. It is responsible for maintaining the game logic
 * and interacts with its listeners.
 */
public class Model {
  private static final Model MODEL = new Model();
  private final List<Listener> listeners = new ArrayList<>();
  private final Board board = Board.getInstance();
  private final PlayerFactory playerFactory = new PlayerFactory();
  private Player[] players;
  private int hand;

  /**
   * Private constructor.
   */
  private Model() {}

  /**
   * Returns the static instance of this class which exercises the singleton pattern.
   *
   * @return The static instance of this class.
   */
  public static Model getInstance() {
    return MODEL;
  }

  /**
   * Adds a listener to notify.
   *
   * @param listener A listener.
   */
  public void addListener(final Listener listener) {
    listeners.add(listener);
  }

  /**
   * Creates two players of MACHINE type or HUMAN type given the desired game mode and resets the
   * starter.
   *
   * @param mode The desired game mode.
   */
  public void setMode(final Mode mode) {
    if (mode.equals(Mode.MANMACHINE)) {
      players = new Player[] {
          playerFactory.getPlayer(PlayerType.MACHINE, ConnectFourSetting.PLAYER_ONE_NAME,
              ConnectFourSetting.PLAYER_ONE_COLOR, board),
          playerFactory.getPlayer(PlayerType.HUMAN, ConnectFourSetting.PLAYER_TWO_NAME,
              ConnectFourSetting.PLAYER_TWO_COLOR, board)};
    } else {
      players = new Player[] {
          playerFactory.getPlayer(PlayerType.HUMAN, ConnectFourSetting.PLAYER_ONE_NAME,
              ConnectFourSetting.PLAYER_ONE_COLOR, board),
          playerFactory.getPlayer(PlayerType.HUMAN, ConnectFourSetting.PLAYER_TWO_NAME,
              ConnectFourSetting.PLAYER_TWO_COLOR, board)};
    }
    hand = 0;
  }

  /**
   * Starts the game. If the player for the first turn is machine, automatically make the first
   * move.
   */
  public void startGame() {
    board.clear();
    fireGameStartedEvent(players[hand].getName());
    if (PlayerType.MACHINE.equals(players[hand].getPlayerType())) {
      makeSingleMove(players[hand].getMoveColumnAdvice());
    }
  }

  /**
   * Makes a move according to the input column. If this move doesn't result in the end of the game
   * and the next player is a MACHINE player, then automatically make the next move.
   *
   * @param column The column to make move.
   */
  public void makeMove(final int column) {
    makeSingleMove(column);
    if (!board.isWon() && !board.isFull()
        && PlayerType.MACHINE.equals(players[hand].getPlayerType())) {
      makeSingleMove(players[hand].getMoveColumnAdvice());
    }
  }

  /**
   * Makes a single move according to the input column. Fires corresponding event according to the
   * game status.
   *
   * @param column The column to make move.
   */
  private void makeSingleMove(final int column) {
    final Move move = board.drop(players[hand].getName(), players[hand].getColor(), column);
    if (move != null) {
      if (board.isWon()) {
        fireGameWonEvent(move);
      } else {
        if (board.isFull()) {
          fireGameTiedEvent(move);
        } else {
          hand = (hand + 1) % 2;
          fireGameContinueEvent(move, players[hand].getName());
        }
      }
    } else {
      fireInvalidMoveMadeEvent(players[hand].getName());
    }
  }

  /**
   * Notifies all listeners that the game has started.
   *
   * @param nextPlayerName The name of the player to do the next turn.
   */
  private void fireGameStartedEvent(final String nextPlayerName) {
    for (final Listener listener : listeners) {
      listener.gameStarted(nextPlayerName);
    }
  }

  /**
   * Notifies all listeners that a move made and the game continues.
   *
   * @param move The last move made.
   * @param nextPlayerName The name of the player to do the next turn.
   */
  private void fireGameContinueEvent(final Move move, final String nextPlayerName) {
    for (final Listener listener : listeners) {
      listener.gameContinue(move, nextPlayerName);
    }
  }

  /**
   * Notifies all listeners that an invalid move was made.
   *
   * @param nextPlayerName The name of the player to do the next turn.
   */
  private void fireInvalidMoveMadeEvent(final String nextPlayerName) {
    for (final Listener listener : listeners) {
      listener.invalidMoveMade(nextPlayerName);
    }
  }

  /**
   * Notifies all listeners that a move was made and the game tied.
   *
   * @param move The last move made.
   */
  private void fireGameTiedEvent(final Move move) {
    for (final Listener listener : listeners) {
      listener.gameTied(move);
    }
  }

  /**
   * Notifies all listeners that a move was made and one player won.
   *
   * @param move The last move made.
   */
  private void fireGameWonEvent(final Move move) {
    for (final Listener listener : listeners) {
      listener.gameWon(move);
    }
  }

  /**
   * Returns the players, for tests only.
   *
   * @return The players of the game.
   */
  Player[] getPlayers() {
    return players;
  }

  /**
   * Returns the string representation of this game model. The string follows the format: "model:
   * {B, players: [P1, P2]}" where B is the information of the board, and P1 and P2 are current
   * players.
   */
  @Override
  public String toString() {
    final List<String> playerStrings = new ArrayList<>();
    for (int i = 0; i < players.length; i++) {
      playerStrings.add(players[i].toString());
    }
    return String.format("model: {%s, players: [%s]}", board.toString(),
        String.join(", ", playerStrings));
  }
}
