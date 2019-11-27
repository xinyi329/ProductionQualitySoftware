package edu.nyu.pqs.ps4.model.player;

import java.awt.Color;

/**
 * This is the interface of player which includes all functions that a player should have.
 */
public interface Player {
  /**
   * Gets the name of the player.
   *
   * @return The name of the player.
   */
  String getName();

  /**
   * Gets the color of the player.
   *
   * @return The color of the player.
   */
  Color getColor();

  /**
   * Gets the type of the player.
   *
   * @return The type of the player.
   */
  PlayerType getPlayerType();

  /**
   * Gets the advice of the next move.
   *
   * @return The column to make the next move.
   */
  int getMoveColumnAdvice();
}
