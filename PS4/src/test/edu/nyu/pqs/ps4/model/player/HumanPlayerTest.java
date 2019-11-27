package edu.nyu.pqs.ps4.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import org.junit.jupiter.api.Test;

class HumanPlayerTest {
  @Test
  public void testBuild_nullName() {
    assertThrows(NullPointerException.class, () -> {
      new HumanPlayer.Builder().name(null).build();
    });
  }

  @Test
  public void testBuild_nullColor() {
    assertThrows(NullPointerException.class, () -> {
      new HumanPlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME).color(null).build();
    });
  }

  @Test
  public void testBuild() {
    final Player player = new HumanPlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).build();
    assertEquals(ConnectFourSetting.PLAYER_ONE_NAME, player.getName());
    assertEquals(ConnectFourSetting.PLAYER_ONE_COLOR, player.getColor());
    assertEquals(PlayerType.HUMAN, player.getPlayerType());
    assertEquals(-1, player.getMoveColumnAdvice());
  }
}
