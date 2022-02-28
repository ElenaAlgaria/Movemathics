package ch.fhnw.ip12.gui.controller;

import ch.fhnw.ip12.gui.manager.HandsManager;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * The controller to the hands.fxml. It is used as overlay to display the hands
 * on every screen.
 */
public class HandController {

  @FXML
  private Pane pane;

  /**
   * Initializes the handsscreen and adds the hands to the screen.
   */
  @FXML
  protected void initialize() {
    HandsManager handsManager = HandsManager.getInstance();
    handsManager.addHands(pane);
  }

}
