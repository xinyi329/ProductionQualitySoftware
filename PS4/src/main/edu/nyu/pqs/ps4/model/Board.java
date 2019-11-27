package edu.nyu.pqs.ps4.model;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class maintains the board and keeps the history of moves. It also checks the status for
 * winning, tied, etc.
 */
public class Board {
  private static final Board BOARD = new Board();
  private final Color[][] board =
      new Color[ConnectFourSetting.COLUMN_NUMBER][ConnectFourSetting.ROW_NUMBER];
  private final LinkedList<Move> moveHistory = new LinkedList<>();

  /**
   * Private constructor.
   */
  private Board() {}

  /**
   * Returns the static instance of this class which exercises the singleton pattern.
   *
   * @return The static instance of this class.
   */
  public static Board getInstance() {
    return BOARD;
  }

  /**
   * Clears the board and history.
   */
  public void clear() {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      for (int j = 0; j < ConnectFourSetting.ROW_NUMBER; j++) {
        board[i][j] = null;
      }
    }
    moveHistory.clear();
  }

  /**
   * Drops a chip to the board and adds the move to the history if succeed.
   *
   * @param playerName The name of the player who makes this move.
   * @param playerColor The color of the player who makes this move.
   * @param column The column selected by the player who makes this move.
   * @return The move made with player name, color, column and row, null if fails to drop.
   */
  public Move drop(final String playerName, final Color playerColor, final int column) {
    if (column >= ConnectFourSetting.COLUMN_NUMBER || column < 0 || isWon()) {
      return null;
    }
    for (int i = 0; i < ConnectFourSetting.ROW_NUMBER; i++) {
      if (board[column][i] == null) {
        final Move move = new Move.Builder().playerName(playerName).playerColor(playerColor)
            .column(column).row(i).build();
        board[column][i] = playerColor;
        moveHistory.add(move);
        return move;
      }
    }
    return null;
  }

  /**
   * Checks whether the board is full or not.
   *
   * @return A boolean indicating the status.
   */
  public boolean isFull() {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      if (board[i][ConnectFourSetting.ROW_NUMBER - 1] == null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether the player who made the last move win the game.
   *
   * @return A boolean indicating the status.
   */
  public boolean isWon() {
    if (moveHistory.size() < 1) {
      return false;
    }
    final Move move = moveHistory.peekLast();
    return isWonInVertical(move) || isWonInHorizontal(move) || isWonInDiagonal(move);
  }

  /**
   * Cancels the last move made with removing it from the history and the board.
   */
  public void cancelLastMove() {
    if (moveHistory.size() > 0) {
      final Move move = moveHistory.pollLast();
      board[move.getColumn()][move.getRow()] = null;
    }
  }

  /**
   * Checks whether the player who made the last move win the game in consecutive moves in vertical.
   *
   * @param move The last move made.
   * @return A boolean indicating the status.
   */
  private boolean isWonInVertical(final Move move) {
    if (move.getRow() < ConnectFourSetting.CONSECUTIVE_COUNT - 1) {
      return false;
    }
    int consecutiveCount = 0;
    for (int i = move.getRow(); i >= 0; i--) {
      if (move.getPlayerColor().equals(board[move.getColumn()][i])) {
        consecutiveCount++;
      } else {
        consecutiveCount = 0;
      }
    }
    if (consecutiveCount == ConnectFourSetting.CONSECUTIVE_COUNT) {
      return true;
    }
    return false;
  }

  /**
   * Checks whether the player who made the last move win the game in consecutive moves in
   * horizontal.
   *
   * @param move The last move made.
   * @return A boolean indicating the status.
   */
  private boolean isWonInHorizontal(final Move move) {
    int consecutiveCount = 0;
    int maxConsecutiveCount = 0;
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      if (move.getPlayerColor().equals(board[i][move.getRow()])) {
        consecutiveCount++;
        maxConsecutiveCount = Math.max(maxConsecutiveCount, consecutiveCount);
      } else {
        consecutiveCount = 0;
      }
    }
    if (maxConsecutiveCount == ConnectFourSetting.CONSECUTIVE_COUNT) {
      return true;
    }
    return false;
  }

  /**
   * Checks whether the player who made the last move win the game in consecutive moves in two
   * diagonals.
   *
   * @param move The last move made.
   * @return A boolean indicating the status.
   */
  private boolean isWonInDiagonal(final Move move) {
    return isWonInDiagonalHelper(move, 1) || isWonInDiagonalHelper(move, -1);
  }

  /**
   * Checks whether the player who made the last move win the game in consecutive moves in diagonal.
   *
   * @param move The last move made.
   * @param c The orientation of the diagonal.
   * @return A boolean indicating the status.
   */
  private boolean isWonInDiagonalHelper(final Move move, final int c) {
    int consecutiveCount = 0;
    int maxConsecutiveCount = 0;
    for (int i =
        -ConnectFourSetting.CONSECUTIVE_COUNT + 1; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      final int column = move.getColumn() + i;
      final int row = move.getRow() + c * i;
      if (column < 0 || column >= ConnectFourSetting.COLUMN_NUMBER || row < 0
          || row >= ConnectFourSetting.ROW_NUMBER) {
        consecutiveCount = 0;
        continue;
      }
      if (move.getPlayerColor().equals(board[column][row])) {
        consecutiveCount++;
        maxConsecutiveCount = Math.max(maxConsecutiveCount, consecutiveCount);
      } else {
        consecutiveCount = 0;
      }
      if (maxConsecutiveCount == ConnectFourSetting.CONSECUTIVE_COUNT) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the board, for tests only.
   *
   * @return The board.
   */
  Color[][] getBoard() {
    return board;
  }

  /**
   * Gets the move history, for tests only.
   *
   * @return The move history.
   */
  List<Move> getMoveHistory() {
    return moveHistory;
  }

  /**
   * Returns the string representation of this board. The string follows the format: "board:
   * {history: [M1, M2, ...]}" where M1, M2, etc. are the moves made.
   */
  @Override
  public String toString() {
    final List<String> moveStrings = new ArrayList<>();
    moveHistory.forEach(m -> {
      moveStrings.add(m.toString());
    });
    return String.format("board: {history: [%s]}", String.join(", ", moveStrings));
  }
}
