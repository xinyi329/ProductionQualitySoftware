package edu.nyu.pqs.ps4.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import edu.nyu.pqs.ps4.model.Board;
import edu.nyu.pqs.ps4.model.Move;
import java.awt.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MachinePlayerTest {
  private static final int COLUMN = 3;
  private static final int ROW = 4;
  private Player player;

  @Mock
  private Board board;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testBuild_nullName() {
    assertThrows(NullPointerException.class, () -> {
      player = new MachinePlayer.Builder().name(null).build();
    });
  }

  @Test
  public void testBuild_nullColor() {
    assertThrows(NullPointerException.class, () -> {
      player =
          new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME).color(null).build();
    });
  }

  @Test
  public void testBuild_nullBoard() {
    assertThrows(NullPointerException.class, () -> {
      player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
          .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(null).build();
    });
  }

  @Test
  public void testBuild() {
    player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(board).build();
    assertEquals(ConnectFourSetting.PLAYER_ONE_NAME, player.getName());
    assertEquals(ConnectFourSetting.PLAYER_ONE_COLOR, player.getColor());
    assertEquals(PlayerType.MACHINE, player.getPlayerType());
  }

  @Test
  public void testGetMoveColumnAdvice_normalMove() {
    final Move move = new Move.Builder().playerName(ConnectFourSetting.PLAYER_ONE_NAME)
        .playerColor(ConnectFourSetting.PLAYER_ONE_COLOR).column(COLUMN).row(ROW).build();
    player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(board).build();
    when(board.drop(anyString(), any(Color.class), anyInt())).thenReturn(move);
    when(board.isWon()).thenReturn(false);
    doNothing().when(board).cancelLastMove();
    final int column = player.getMoveColumnAdvice();
    verify(board, times(ConnectFourSetting.COLUMN_NUMBER)).drop(anyString(), any(Color.class),
        anyInt());
    player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(board).build();
    assertEquals(COLUMN, column);
  }

  @Test
  public void testGetMoveColumnAdvice_moveToWin() {
    final Move move = new Move.Builder().playerName(ConnectFourSetting.PLAYER_ONE_NAME)
        .playerColor(ConnectFourSetting.PLAYER_ONE_COLOR).column(COLUMN).row(ROW).build();
    player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(board).build();
    when(board.drop(anyString(), any(Color.class), anyInt())).thenReturn(move);
    when(board.isWon()).thenReturn(true);
    doNothing().when(board).cancelLastMove();
    final int column = player.getMoveColumnAdvice();
    verify(board, times(1)).drop(anyString(), any(Color.class), anyInt());
    player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(board).build();
    assertEquals(COLUMN, column);
  }

  @Test
  public void testGetMoveColumnAdvice_full() {
    when(board.drop(anyString(), any(Color.class), anyInt())).thenReturn(null);
    player = new MachinePlayer.Builder().name(ConnectFourSetting.PLAYER_ONE_NAME)
        .color(ConnectFourSetting.PLAYER_ONE_COLOR).board(board).build();
    final int column = player.getMoveColumnAdvice();
    verify(board, times(ConnectFourSetting.COLUMN_NUMBER)).drop(anyString(), any(Color.class),
        anyInt());
    verify(board, times(0)).isWon();
    verify(board, times(0)).cancelLastMove();
    assertEquals(-1, column);
  }
}
