package edu.nyu.pqs.ps4.view;

import edu.nyu.pqs.ps4.ConnectFourSetting;
import edu.nyu.pqs.ps4.model.Mode;
import edu.nyu.pqs.ps4.model.Model;
import edu.nyu.pqs.ps4.model.Move;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * This class is an implementation of the listener to the game model. It provides a GUI that listens
 * to and interacts with that, and perform behaviors accordingly.
 */
public class View implements Listener {
  private final Model model;
  private final JLabel[][] board =
      new JLabel[ConnectFourSetting.ROW_NUMBER][ConnectFourSetting.COLUMN_NUMBER];
  private final JButton[] dropButtons = new JButton[ConnectFourSetting.COLUMN_NUMBER];
  private final JButton[] startButtons = new JButton[ConnectFourSetting.MODE_NUMBER];
  private final JTextArea information = new JTextArea();

  /**
   * Public constructor that takes the model that this class listens to and creates a GUI that users
   * can interact with. The GUI includes two start buttons to start with different modes
   * (manmachine, multiplayer), a 7x6 board with 7 corresponding drop buttons, and an text area to
   * show the information. The drop buttons are disabled when the game is not ongoing.
   *
   * @param m The model to listen to.
   */
  public View(final Model m) {
    model = m;
    model.addListener(this);

    final JFrame frame = new JFrame("Connect Four");
    final JPanel panel = new JPanel(new BorderLayout());
    final JPanel leftPanel = new JPanel(new BorderLayout());
    final JPanel dropPanel = new JPanel(new GridLayout(0, ConnectFourSetting.COLUMN_NUMBER));
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      dropButtons[i] = new JButton("Drop");
      dropButtons[i].addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          makeMove(e.getSource());
        }
      });
      dropButtons[i].setEnabled(false);
      dropPanel.add(dropButtons[i]);
    }
    final JPanel boardPanel =
        new JPanel(new GridLayout(ConnectFourSetting.ROW_NUMBER, ConnectFourSetting.COLUMN_NUMBER));
    for (int i = 0; i < ConnectFourSetting.ROW_NUMBER; i++) {
      for (int j = 0; j < ConnectFourSetting.COLUMN_NUMBER; j++) {
        board[i][j] = new JLabel();
        board[i][j].setOpaque(true);
        board[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        board[i][j].setBackground(Color.LIGHT_GRAY);
        board[i][j].setHorizontalAlignment(SwingConstants.CENTER);
        board[i][j].setVerticalAlignment(SwingConstants.CENTER);
        boardPanel.add(board[i][j]);
      }
    }
    leftPanel.add(dropPanel, BorderLayout.NORTH);
    leftPanel.add(boardPanel, BorderLayout.CENTER);
    final JPanel rightPanel = new JPanel(new BorderLayout());
    final JPanel startPanel = new JPanel(new GridLayout(ConnectFourSetting.MODE_NUMBER, 0));
    startButtons[0] = new JButton("Start Manmachine Game");
    startButtons[0].addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        startManmachineGame();
      }
    });
    startButtons[1] = new JButton("Start Multiplayer Game");
    startButtons[1].addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        startMultiplayerGame();
      }
    });
    startPanel.add(startButtons[0]);
    startPanel.add(startButtons[1]);
    information.setLineWrap(true);
    rightPanel.add(startPanel, BorderLayout.NORTH);
    rightPanel.add(new JScrollPane(information), BorderLayout.CENTER);
    panel.add(leftPanel, BorderLayout.CENTER);
    panel.add(rightPanel, BorderLayout.EAST);
    frame.getContentPane().add(panel);
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Performs actions including clearing the board panel, enabling drop buttons, and showing the
   * game status in information text area when this listener is notified that the game has started.
   */
  @Override
  public void gameStarted(final String nextPlayerName) {
    for (int i = 0; i < ConnectFourSetting.ROW_NUMBER; i++) {
      for (int j = 0; j < ConnectFourSetting.COLUMN_NUMBER; j++) {
        board[i][j].setText("");
        board[i][j].setBackground(Color.LIGHT_GRAY);
      }
    }
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      dropButtons[i].setEnabled(true);
    }
    information.append(String.format("Game Started.\n%s take this turn.\n", nextPlayerName));
  }

  /**
   * Performs actions including displaying the move in board panel, and showing the game status in
   * information text area when this listener is notified that a move was made and the game
   * continues.
   */
  @Override
  public void gameContinue(final Move move, final String nextPlayerName) {
    board[getRowInPanel(move.getRow())][move.getColumn()].setText(move.getPlayerName());
    board[getRowInPanel(move.getRow())][move.getColumn()].setBackground(move.getPlayerColor());
    information.append(String.format("%s dropped at column %d.\n%s take this turn.\n",
        move.getPlayerName(), move.getColumn() + 1, nextPlayerName));
  }

  /**
   * Performs actions including showing the game status that the previous player is required to redo
   * the move in information text area when this listener is notified that an invalid move was made.
   */
  @Override
  public void invalidMoveMade(final String nextPlayerName) {
    information.append(String.format("%s made an invalid move.\n%s take this turn.\n",
        nextPlayerName, nextPlayerName));
  }

  /**
   * Performs actions including disabling the drop buttons, displaying the move in board panel, and
   * showing the game status in information text area when this listener is notified that a move was
   * made and the game tied.
   */
  @Override
  public void gameTied(final Move move) {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      dropButtons[i].setEnabled(false);
    }
    board[getRowInPanel(move.getRow())][move.getColumn()].setText(move.getPlayerName());
    board[getRowInPanel(move.getRow())][move.getColumn()].setBackground(move.getPlayerColor());
    information.append(
        String.format("%s dropped at column %d.\nGame tied. Please restart or exit the game.\n",
            move.getPlayerName(), move.getColumn() + 1));
  }

  /**
   * Performs actions including disabling the drop buttons, displaying the move in board panel, and
   * showing the game status in information text area when this listener is notified that a move was
   * made and one player won.
   */
  @Override
  public void gameWon(final Move move) {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      dropButtons[i].setEnabled(false);
    }
    board[getRowInPanel(move.getRow())][move.getColumn()].setText(move.getPlayerName());
    board[getRowInPanel(move.getRow())][move.getColumn()].setBackground(move.getPlayerColor());
    information.append(
        String.format("%s dropped at column %d.\n%s won. Please restart or exit the game.\n",
            move.getPlayerName(), move.getColumn() + 1, move.getPlayerName()));
  }

  /**
   * Translates the row number stored in board to the row number to display in GUI as rows with
   * smaller indices are displayed in upper side.
   *
   * @param row The row number stored in board.
   * @return The row number to display in GUI.
   */
  private int getRowInPanel(final int row) {
    return (ConnectFourSetting.ROW_NUMBER - 1 - row);
  }

  /**
   * Interacts with the game model including setting the correct game mode and notifying the game to
   * start when target button is pressed.
   */
  private void startManmachineGame() {
    model.setMode(Mode.MANMACHINE);
    model.startGame();
  }

  /**
   * Interacts with the game model including setting the correct game mode and notifying the game to
   * start when target button is pressed.
   */
  private void startMultiplayerGame() {
    model.setMode(Mode.MULTIPLAYER);
    model.startGame();
  }

  /**
   * Interacts with the game model including notifying the column number that a player has chosen
   * when target button is pressed.
   */
  private void makeMove(final Object source) {
    for (int i = 0; i < ConnectFourSetting.COLUMN_NUMBER; i++) {
      if (source.equals(dropButtons[i])) {
        model.makeMove(i);
        break;
      }
    }
  }

  /**
   * Returns the string representation of this listener. The string follows the format: "{listener:
   * {type: GUI, M}}", where M is the details of the game model that this class listens to.
   */
  @Override
  public String toString() {
    return String.format("listener: {type: GUI, %s}", model.toString());
  }
}
