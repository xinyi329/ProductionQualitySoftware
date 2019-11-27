package edu.nyu.pqs.ps4.model;

import java.awt.Color;
import java.util.Objects;

/**
 * This class consists of elements that defines a move made, including player name, player color,
 * column and row.
 */
public class Move {
  private final String playerName;
  private final Color playerColor;
  private final int column;
  private final int row;

  /**
   * Builder class.
   */
  public static class Builder {
    private String playerName;
    private Color playerColor;
    private int column;
    private int row;

    /**
     * Constructor.
     */
    public Builder() {}

    /**
     * Sets player name when building an instance.
     *
     * @param pn The input player name.
     * @return The altered builder class.
     */
    public Builder playerName(final String pn) {
      playerName = pn;
      return this;
    }

    /**
     * Sets player color when building an instance.
     *
     * @param pc The input player color.
     * @return The altered builder class.
     */
    public Builder playerColor(final Color pc) {
      playerColor = pc;
      return this;
    }

    /**
     * Sets column when building an instance.
     *
     * @param c The input column.
     * @return The altered builder class.
     */
    public Builder column(final int c) {
      column = c;
      return this;
    }

    /**
     * Sets row when building an instance.
     *
     * @param r The input row.
     * @return The altered builder class.
     */
    public Builder row(final int r) {
      row = r;
      return this;
    }

    /**
     * Builds the move instance. Require player name and player color to be non-null values.
     *
     * @return The built move instance.
     */
    public Move build() {
      Objects.requireNonNull(playerName);
      Objects.requireNonNull(playerColor);
      return new Move(this);
    }
  }

  /**
   * Private constructor.
   *
   * @param builder Builder.
   */
  private Move(final Builder builder) {
    playerName = builder.playerName;
    playerColor = builder.playerColor;
    column = builder.column;
    row = builder.row;
  }

  /**
   * Returns the player name.
   *
   * @return The player name.
   */
  public String getPlayerName() {
    return playerName;
  }

  /**
   * Returns the player color.
   *
   * @return The player color.
   */
  public Color getPlayerColor() {
    return playerColor;
  }

  /**
   * Returns the row.
   *
   * @return The row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column.
   *
   * @return The column.
   */
  public int getColumn() {
    return column;
  }

  /**
   * Returns the string representation of the move instance. The string follows the format: "{name:
   * PN, color: PC, row: R, column: C}" where PN is the player name, PC is the player color, R is
   * the row number, and C is the column number.
   */
  @Override
  public String toString() {
    return String.format("{name: %s, color: %s, row: %d, column: %d}", playerName, playerColor, row,
        column);
  }
}
