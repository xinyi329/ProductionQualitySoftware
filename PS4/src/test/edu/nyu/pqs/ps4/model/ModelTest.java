package edu.nyu.pqs.ps4.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import edu.nyu.pqs.ps4.model.player.Player;
import edu.nyu.pqs.ps4.model.player.PlayerType;
import edu.nyu.pqs.ps4.view.Listener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModelTest {
  private final Model model = Model.getInstance();

  @Mock
  private Listener listener;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSetMode_manMachine() {
    model.setMode(Mode.MANMACHINE);
    final Player[] players = model.getPlayers();
    assertEquals(2, players.length);
    assertEquals(PlayerType.MACHINE, players[0].getPlayerType());
    assertEquals(PlayerType.HUMAN, players[1].getPlayerType());
  }

  @Test
  public void testSetMode_multiPlayer() {
    model.setMode(Mode.MULTIPLAYER);
    final Player[] players = model.getPlayers();
    assertEquals(2, players.length);
    assertEquals(PlayerType.HUMAN, players[0].getPlayerType());
    assertEquals(PlayerType.HUMAN, players[1].getPlayerType());
  }

  @Test
  public void testGame_manmachine() {
    model.setMode(Mode.MANMACHINE);
    model.addListener(listener);
    model.startGame();
    verify(listener, times(1)).gameStarted(anyString());
    verify(listener, times(1)).gameContinue(any(Move.class),
        eq(ConnectFourSetting.PLAYER_TWO_NAME));
    model.makeMove(0);
    verify(listener, times(1)).gameContinue(any(Move.class),
        eq(ConnectFourSetting.PLAYER_ONE_NAME));
    verify(listener, times(2)).gameContinue(any(Move.class),
        eq(ConnectFourSetting.PLAYER_TWO_NAME));
  }

  @Test
  public void testGame_multiplayer_won() {
    model.setMode(Mode.MULTIPLAYER);
    model.addListener(listener);
    model.startGame();
    verify(listener, times(1)).gameStarted(anyString());
    verify(listener, times(0)).gameContinue(any(Move.class), anyString());
    model.makeMove(-1);
    verify(listener, times(1)).invalidMoveMade(ConnectFourSetting.PLAYER_ONE_NAME);
    for (int i = 1; i < ConnectFourSetting.CONSECUTIVE_COUNT; i++) {
      model.makeMove(0);
      verify(listener, times(i)).gameContinue(any(Move.class),
          eq(ConnectFourSetting.PLAYER_TWO_NAME));
      model.makeMove(1);
      verify(listener, times(i)).gameContinue(any(Move.class),
          eq(ConnectFourSetting.PLAYER_ONE_NAME));
    }
    model.makeMove(0);
    verify(listener, times(1)).gameWon(any(Move.class));
    model.makeMove(0);
    verify(listener, times(2)).invalidMoveMade(ConnectFourSetting.PLAYER_ONE_NAME);
  }

  @Test
  public void testGame_multiplayer_tied() {
    model.setMode(Mode.MULTIPLAYER);
    model.addListener(listener);
    model.startGame();
    verify(listener, times(1)).gameStarted(anyString());
    int t = 0;
    for (int i = 1; i < 2 * ConnectFourSetting.COLUMN_NUMBER; i += 2) {
      for (int j = 1; j < ConnectFourSetting.CONSECUTIVE_COUNT; j++) {
        model.makeMove((i - 1) % ConnectFourSetting.COLUMN_NUMBER);
        model.makeMove(i % ConnectFourSetting.COLUMN_NUMBER);
        t++;
      }
    }
    verify(listener, times(t)).gameContinue(any(Move.class),
        eq(ConnectFourSetting.PLAYER_TWO_NAME));
    verify(listener, times(t - 1)).gameContinue(any(Move.class),
        eq(ConnectFourSetting.PLAYER_ONE_NAME));
    verify(listener, times(1)).gameTied(any(Move.class));
  }
}
