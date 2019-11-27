package edu.nyu.pqs.ps4.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
  private final Board board = Board.getInstance();

  @BeforeEach
  public void setup() {
    board.clear();
  }

  @Test
  public void testClear() {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      for (int j = 0; j < ConnectFourSetting.ROW_NUMBER; j++) {
        assertNull(board.getBoard()[i][j]);
      }
    }
    assertEquals(0, board.getMoveHistory().size());
  }

  @Test
  public void testDrop() {
    final Move move =
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
    assertEquals(ConnectFourSetting.PLAYER_ONE_NAME, move.getPlayerName());
    assertEquals(ConnectFourSetting.PLAYER_ONE_COLOR, move.getPlayerColor());
    assertEquals(0, move.getColumn());
    assertEquals(0, move.getRow());
    assertEquals(1, board.getMoveHistory().size());
    assertEquals(ConnectFourSetting.PLAYER_ONE_COLOR,
        board.getBoard()[move.getColumn()][move.getRow()]);
  }

  @Test
  public void testDrop_outOfBound_1() {
    final Move move =
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, -1);
    assertNull(move);
  }

  @Test
  public void testDrop_outOfBound_2() {
    final Move move = board.drop(ConnectFourSetting.PLAYER_ONE_NAME,
        ConnectFourSetting.PLAYER_ONE_COLOR, ConnectFourSetting.COLUMN_NUMBER + 1);
    assertNull(move);
  }

  @Test
  public void testDrop_fullColumn() {
    for (int i = 0; i < (ConnectFourSetting.ROW_NUMBER + 1) / 2; i++) {
      board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
      board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, 0);
    }
    final Move move =
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
    assertNull(move);
  }

  @Test
  public void testDrop_gameWon() {
    for (int i = 0; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
    }
    final Move move =
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
    assertNull(move);
  }

  @Test
  public void testIsFull_empty() {
    assertFalse(board.isFull());
  }

  @Test
  public void testIsFull_halfFull() {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i += 2) {
      for (int j = 0; j < (ConnectFourSetting.ROW_NUMBER + 1) / 2; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
        board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
      }
    }
    assertFalse(board.isFull());
  }

  @Test
  public void testIsFull_full() {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i += 2) {
      for (int j = 0; j < ConnectFourSetting.CONSECUTIVE_COUNT - 1; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
      }
      for (int j = 0; j < ConnectFourSetting.CONSECUTIVE_COUNT - 1; j++) {
        board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
      }
    }
    for (int i = 1; i < ConnectFourSetting.COLUMN_NUMBER; i += 2) {
      for (int j = 0; j < ConnectFourSetting.CONSECUTIVE_COUNT - 1; j++) {
        board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
      }
      for (int j = 0; j < ConnectFourSetting.CONSECUTIVE_COUNT - 1; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
      }
    }
    assertTrue(board.isFull());
  }

  @Test
  public void testIsWon_empty() {
    assertFalse(board.isWon());
  }

  @Test
  public void testIsWon_halfFull() {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i += 2) {
      for (int j = 0; j < (ConnectFourSetting.ROW_NUMBER + 1) / 2; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
        board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
      }
    }
    assertFalse(board.isWon());
  }

  @Test
  public void testIsWon_wonInVertical() {
    for (int i = 0; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
    }
    assertTrue(board.isWon());
  }

  @Test
  public void testIsWon_wonInHorizontal() {
    for (int i = 0; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
    }
    assertTrue(board.isWon());
  }

  @Test
  public void testIsWon_wonInDiagonal_lowerLeftToUpperRight() {
    for (int i = 0; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      for (int j = 0; j < i; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
      }
      board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
    }
    assertTrue(board.isWon());
  }

  @Test
  public void testIsWon_wonInDiagonal_upperLeftToLowerRight() {
    for (int i = 0; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      for (int j = i; j < ConnectFourSetting.CONSECUTIVE_COUNT - 1; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
      }
      board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
    }
    assertTrue(board.isWon());
  }

  @Test
  public void testCancelLastMove_noMove() {
    board.cancelLastMove();
    assertEquals(0, board.getMoveHistory().size());
  }

  @Test
  public void testCancelLastMove_normalMove() {
    final Move move =
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, 0);
    board.cancelLastMove();
    assertEquals(0, board.getMoveHistory().size());
    assertNull(board.getBoard()[move.getColumn()][move.getRow()]);
  }

  @Test
  public void testCancelLastMove_movetoWin() {
    for (int i = 1; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      for (int j = 0; j < i; j++) {
        board.drop(ConnectFourSetting.PLAYER_ONE_NAME, ConnectFourSetting.PLAYER_ONE_COLOR, i);
      }
      board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, i);
    }
    final Move move =
        board.drop(ConnectFourSetting.PLAYER_TWO_NAME, ConnectFourSetting.PLAYER_TWO_COLOR, 0);
    final int moveHistorySizeBeforeCancel = board.getMoveHistory().size();
    board.cancelLastMove();
    assertEquals(moveHistorySizeBeforeCancel - 1, board.getMoveHistory().size());
    assertNull(board.getBoard()[move.getColumn()][move.getRow()]);
    assertFalse(board.isWon());
  }
}
