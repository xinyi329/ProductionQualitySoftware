package edu.nyu.pqs.ps4;

import edu.nyu.pqs.ps4.model.Model;
import edu.nyu.pqs.ps4.view.View;

/**
 * This is the main class to run the Connect Four program.
 */
public class ConnectFourApp {
  /**
   * Runs the Connect Four application.
   */
  public static void main(final String[] args) {
    new ConnectFourApp().run();
  }

  /**
   * Gets a game model instance and sets up a GUI.
   */
  private void run() {
    final Model model = Model.getInstance();
    new View(model);
  }
}
