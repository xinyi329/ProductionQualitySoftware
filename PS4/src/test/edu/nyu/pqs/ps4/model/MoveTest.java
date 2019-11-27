package edu.nyu.pqs.ps4.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import org.junit.jupiter.api.Test;

class MoveTest {
  private static final int COLUMN = 3;
  private static final int ROW = 4;

  @Test
  public void testBuild_nullPlayerName() {
    assertThrows(NullPointerException.class, () -> {
      new Move.Builder().playerName(null).build();
    });
  }

  @Test
  public void testBuild_nullPlayerColor() {
    assertThrows(NullPointerException.class, () -> {
      new Move.Builder().playerName(ConnectFourSetting.PLAYER_ONE_NAME).playerColor(null).build();
    });
  }

  @Test
  public void testBuild() {
    final Move move = new Move.Builder().playerName(ConnectFourSetting.PLAYER_ONE_NAME)
        .playerColor(ConnectFourSetting.PLAYER_ONE_COLOR).column(COLUMN).row(ROW).build();
    assertEquals(ConnectFourSetting.PLAYER_ONE_NAME, move.getPlayerName());
    assertEquals(ConnectFourSetting.PLAYER_ONE_COLOR, move.getPlayerColor());
    assertEquals(COLUMN, move.getColumn());
    assertEquals(ROW, move.getRow());
  }
}
