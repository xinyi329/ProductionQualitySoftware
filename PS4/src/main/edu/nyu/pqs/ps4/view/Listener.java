package edu.nyu.pqs.ps4.view;

import edu.nyu.pqs.ps4.model.Move;

/**
 * This is the interface of a listener to the game model.
 */
public interface Listener {
  /**
   * Performs actions to do when the listener is notified that the game has started.
   *
   * @param nextPlayerName The name of the player to do the next turn.
   */
  void gameStarted(String nextPlayerName);

  /**
   * Performs actions to do when the listener is notified that a move was made and the game
   * continues.
   *
   * @param move The last move made.
   * @param nextPlayerName The name of the player to do the next turn.
   */
  void gameContinue(Move move, String nextPlayerName);

  /**
   * Performs actions to do when the listener is notified that an invalid move was made.
   *
   * @param nextPlayerName The name of the player to do the next turn.
   */
  void invalidMoveMade(String nextPlayerName);

  /**
   * Performs actions to do when the listener is notified that a move was made and the game tied.
   *
   * @param move The last move made.
   */
  void gameTied(Move move);

  /**
   * Performs actions to do when the listener is notified that a move was made and one player won.
   *
   * @param move The last move made.
   */
  void gameWon(Move move);
}
