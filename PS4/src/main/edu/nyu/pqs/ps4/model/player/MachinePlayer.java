package edu.nyu.pqs.ps4.model.player;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import edu.nyu.pqs.ps4.model.Board;
import edu.nyu.pqs.ps4.model.Move;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * This class is an implementation of player as an machine player that needs to see the board in the
 * program to make decisions about moves.
 */
public class MachinePlayer implements Player {
  private static final PlayerType PLAYER_TYPE = PlayerType.MACHINE;
  private final String name;
  private final Color color;
  private final Board board;

  /**
   * Builder class.
   */
  public static class Builder {
    private String name;
    private Color color;
    private Board board;

    /**
     * Constructor.
     */
    public Builder() {}

    /**
     * Sets name when building an instance.
     *
     * @param n The input name.
     * @return The altered builder class.
     */
    public Builder name(final String n) {
      name = n;
      return this;
    }

    /**
     * Sets color when building an instance.
     *
     * @param c The input color.
     * @return The altered builder class.
     */
    public Builder color(final Color c) {
      color = c;
      return this;
    }

    /**
     * Sets board when building an instance.
     *
     * @param b The input board.
     * @return The altered builder class.
     */
    public Builder board(final Board b) {
      board = b;
      return this;
    }

    /**
     * Builds the machine player instance. Require name, color and board to be non-null values.
     *
     * @return The built machine player instance.
     */
    public MachinePlayer build() {
      Objects.requireNonNull(name);
      Objects.requireNonNull(color);
      Objects.requireNonNull(board);
      return new MachinePlayer(this);
    }
  }

  /**
   * Private constructor.
   *
   * @param builder Builder.
   */
  private MachinePlayer(final Builder builder) {
    name = builder.name;
    color = builder.color;
    board = builder.board;
  }

  /**
   * Gets the name of the player.
   *
   * @return The name of the player.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Gets the color of the player.
   *
   * @return The color of the player.
   */
  @Override
  public Color getColor() {
    return color;
  }

  /**
   * Gets the type of the player.
   *
   * @return The type of the player.
   */
  @Override
  public PlayerType getPlayerType() {
    return PLAYER_TYPE;
  }

  /**
   * Gets the advice of the move to a win, otherwise a random column that is not full.
   *
   * @return The column to make the next move.
   */
  @Override
  public int getMoveColumnAdvice() {
    final List<Integer> possibleColumns = new ArrayList<>();
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      final Move move = board.drop(name, color, i);
      if (move != null) {
        final boolean isWon = board.isWon();
        board.cancelLastMove();
        if (isWon) {
          return move.getColumn();
        } else {
          possibleColumns.add(move.getColumn());
        }
      }
    }
    return possibleColumns.size() > 0
        ? possibleColumns.get(new Random().nextInt(possibleColumns.size()))
        : -1;
  }

  /**
   * Returns the string representation of the player instance. The string follows the format:
   * "{name: N, color: C, type: MACHINE}" where N is the name and C is the color.
   */
  @Override
  public String toString() {
    return String.format("{name: %s, color: %s, type: %s}", name, color.toString(),
        PLAYER_TYPE.name());
  }
}
