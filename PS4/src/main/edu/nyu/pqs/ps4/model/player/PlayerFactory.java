package edu.nyu.pqs.ps4.model.player;

import edu.nyu.pqs.ps4.model.Board;
import java.awt.Color;

/**
 * This class provide factory method to build instances of human players or machine players.
 */
public class PlayerFactory {
  /**
   * Builds instances of human players or machine players as requested.
   *
   * @param playerType The type of the player.
   * @param name The name of the player.
   * @param color The color of the player.
   * @param board The board of the game.
   * @return The desired player.
   */
  public Player getPlayer(final PlayerType playerType, final String name, final Color color,
      final Board board) {
    if (playerType.equals(PlayerType.HUMAN)) {
      return new HumanPlayer.Builder().name(name).color(color).build();
    } else {
      return new MachinePlayer.Builder().name(name).color(color).board(board).build();
    }
  }
}
