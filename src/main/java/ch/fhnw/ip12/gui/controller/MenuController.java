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
import javafx.scene.layout.Pane;

/**
 * Class description: Controller class of "Menu" scene
 */

public class MenuController implements HandControllable {
  private FxGameButton restartButton;
  private FxGameButton quitButton;
  private FxGameButton resumeButton;
  private ScenesManager scenesManager;
  private static final ScreenName ID = ScreenName.MENU_SCREEN;

  @FXML
  private Pane pane;
  @FXML
  private Button restartGameButtonFX;
  @FXML
  private Button quitGameButtonFX;
  @FXML
  private Button resumeGameButtonFX;
  @FXML
  private ImageView stonewallMenu;
  @FXML
  private ImageView stonewallMirrorMenu;

  /**
   * Is called when the hands have been moved and handles intersections with the
   * buttons.
   * 
   * @param leftHand  the left hand
   * @param rightHand the right hand
   */
  @Override
  public void handsMoved(Hand leftHand, Hand rightHand) {
    if (restartButton == null || restartButton.getX() == 0) {
      this.restartButton = new FxGameButton(restartGameButtonFX, ColorConstants.GREEN);
      this.quitButton = new FxGameButton(quitGameButtonFX, ColorConstants.RED);
      this.resumeButton = new FxGameButton(resumeGameButtonFX, ColorConstants.BLUE);
    }
    handleIntersection(restartButton);
    handleIntersection(resumeButton);
    handleIntersection(quitButton);
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
    HandsManager handsManager = HandsManager.getInstance();
    handsManager.register(this);
    this.scenesManager = ScenesManager.getInstance();
    changeSizeOfStonewall();
  }

  /**
   * Gets called when the setOnAction Event of the quit game button is invoked and
   * switches to the startscreen.
   * 
   * @param event
   */
  @FXML
  protected void quitGameBtn(ActionEvent event) {
    scenesManager.switchScene(ScreenName.START_SCREEN);
  }

  /**
   * Gets called when the setOnAction Event of the restart game button is invoked
   * and switches to the calibrationscreen.
   * 
   * @param event
   */
  @FXML
  protected void restartGameBtn(ActionEvent event) {
    scenesManager.switchScene(ScreenName.CALIBRATION_SCREEN, true);
  }

  /**
   * Gets called when the setOnAction Event of the resume game button is invoked,
   * switches back to the gamescreen and starts a countdown.
   * 
   * @param event
   */
  @FXML
  protected void resumeGameBtn(ActionEvent event) {
    SoundManager.COUNTDOWN.getMusic();
    scenesManager.switchScene(ScreenName.GAME_SCREEN);
  }

  /**
   * Updates the stonewall size to the actual size of the screen in order to fill
   * the whol screen width.
   */
  private void changeSizeOfStonewall() {
    double width = MoveMathics.getStageWidth() / 2;
    double height = MoveMathics.getStageHeight() / 3.5;
    stonewallMenu.setFitHeight(height);
    stonewallMenu.setFitWidth(width);
    stonewallMirrorMenu.setFitHeight(height);
    stonewallMirrorMenu.setFitWidth(width);
    stonewallMenu.setViewOrder(100001);
    stonewallMirrorMenu.setViewOrder(100000);
  }

  /**
   * Handles the intersections with the menu buttons.
   */
  private void handleIntersection(FxGameButton fxGameButton) {
    HandsManager handsManager = HandsManager.getInstance();
    if (handsManager.oneHandIntersects(fxGameButton) != null) {
      fxGameButton.fireEventWithAnimation();
    }
  }

}