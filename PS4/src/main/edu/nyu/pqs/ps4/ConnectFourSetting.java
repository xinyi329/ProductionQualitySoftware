package edu.nyu.pqs.ps4;

import java.awt.Color;

/**
 * This class stores the parameters of the Connect Four game. The game requires PLAYER_ONE_COLOR and
 * PLAYER_TWO_COLOR to be different. Some parameters like player names and colors may be
 * self-configured in the future, but this isn't supported yet in this version.
 */
public class ConnectFourSetting {
  public static final int COLUMN_NUMBER = 7;
  public static final int ROW_NUMBER = 6;
  public static final int CONSECUTIVE_COUNT = 4;
  public static final int MODE_NUMBER = 2;
  public static final String PLAYER_ONE_NAME = "RED";
  public static final Color PLAYER_ONE_COLOR = Color.RED;
  public static final String PLAYER_TWO_NAME = "BLUE";
  public static final Color PLAYER_TWO_COLOR = Color.BLUE;
}
