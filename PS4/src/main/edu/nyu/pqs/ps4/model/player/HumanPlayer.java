package edu.nyu.pqs.ps4.model.player;

import java.awt.Color;
import java.util.Objects;

/**
 * This class is an implementation of player as an human player.
 */
public class HumanPlayer implements Player {
  private static final PlayerType PLAYER_TYPE = PlayerType.HUMAN;
  private final String name;
  private final Color color;

  /**
   * Builder class.
   */
  public static class Builder {
    private String name;
    private Color color;

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
     * Builds the human player instance. Require name and color to be non-null values.
     *
     * @return The built human player instance.
     */
    public HumanPlayer build() {
      Objects.requireNonNull(name);
      Objects.requireNonNull(color);
      return new HumanPlayer(this);
    }
  }

  /**
   * Private constructor.
   *
   * @param builder Builder.
   */
  private HumanPlayer(final Builder builder) {
    name = builder.name;
    color = builder.color;
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
   * Gets the advice of the next move but human should have the idea in their brain but not in
   * program. Returns an invalid move if relying on this function.
   *
   * @return The column to make an invalid move.
   */
  @Override
  public int getMoveColumnAdvice() {
    return -1;
  }

  /**
   * Returns the string representation of the player instance. The string follows the format:
   * "{name: N, color: C, type: HUMAN}" where N is the name and C is the color.
   */
  @Override
  public String toString() {
    return String.format("{name: %s, color: %s, type: %s}", name, color.toString(),
        PLAYER_TYPE.name());
  }
}
