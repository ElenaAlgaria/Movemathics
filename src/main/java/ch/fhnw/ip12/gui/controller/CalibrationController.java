package ch.fhnw.ip12.gui.controller;

import ch.fhnw.ip12.MoveMathics;
import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.FxGameButton;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.animation.ColorConstants;
import ch.fhnw.ip12.gui.input.HandControllable;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.manager.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Class description: Controller class of "kalibrierung" scene
 */

public class CalibrationController implements HandControllable {

  @FXML
  private Button leftHandRectFX;
  @FXML
  private Button rightHandRectFX;
  @FXML
  private BorderPane pane;
  @FXML
  private ImageView steinmauer;

  @FXML
  private ImageView steinmauerMirror;

  private HandsManager handsManager;
  private FxGameButton leftHandRect;
  private FxGameButton rightHandRect;
  private static final ScreenName ID = ScreenName.CALIBRATION_SCREEN;

  /**
   * Is called when the hands have been moved and handles intersections with the
   * buttons.
   * 
   * @param leftHand  the left hand
   * @param rightHand the right hand
   */
  @Override
  public void handsMoved(Hand leftHand, Hand rightHand) {
    if (this.leftHandRect == null || this.leftHandRect.getX() == 0) {
      this.leftHandRect = new FxGameButton(leftHandRectFX, ColorConstants.GREEN);
      this.rightHandRect = new FxGameButton(rightHandRectFX, ColorConstants.GREEN);

    }
    handleIntersection();
  }

  @Override
  public ScreenName getID() {
    return ID;
  }

  /**
   * Initializes the controller, this includes: all controls, register at the
   * handsmanger and to update the stonewall size.
   */
  @FXML
  protected void initialize() {
    this.handsManager = HandsManager.getInstance();
    this.handsManager.register(this);
    changeSizeOfStonewall();
  }

  /**
   * Gets called when the setOnAction Event of the calibration buttons is invoked
   * and switches to the gamescreen.
   * 
   * @param event the event delivered by the setOnAction Event.
   */
  @FXML
  protected void startGame(ActionEvent event) {
    SoundManager.SELECT_BUTTON.getMusic();
    ScenesManager.getInstance().switchScene(ScreenName.GAME_SCREEN, true);
  }

  /**
   * Updates the stonewall size to the actual size of the screen in order to fill
   * the whol screen width.
   */
  private void changeSizeOfStonewall() {
    double width = MoveMathics.getStageWidth() / 2;
    double height = MoveMathics.getStageHeight() / 3.5;
    steinmauer.setFitHeight(height);
    steinmauer.setFitWidth(width);
    steinmauerMirror.setFitHeight(height);
    steinmauerMirror.setFitWidth(width);
  }

  /**
   * Handles the intersections with the calibration buttons.
   */
  private void handleIntersection() {
    if (handsManager.oneHandIntersects(leftHandRect) != null && handsManager.oneHandIntersects(rightHandRect) != null) {
      leftHandRect.fireEventWithAnimation();
      rightHandRect.fireEventWithAnimation();
    } else {
      leftHandRect.stopFireEventWithAnimation();
      rightHandRect.stopFireEventWithAnimation();
    }
  }
}
