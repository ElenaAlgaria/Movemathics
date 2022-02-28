package ch.fhnw.ip12.gui.controller;

import java.io.IOException;

import ch.fhnw.ip12.MoveMathics;
import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.FxGameButton;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.animation.ColorConstants;
import ch.fhnw.ip12.gui.input.HandControllable;
import ch.fhnw.ip12.gui.io.ExerciseCsvIO;
import ch.fhnw.ip12.gui.io.MoveMathicsCsvIO;
import ch.fhnw.ip12.gui.io.MoveMathicsFiles;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Class description: Controller class of Start scene
 */

public class StartScreenController implements HandControllable {

  @FXML
  private TextField nameTxt;

  @FXML
  private Button gameStartButtonFX;

  @FXML
  private Button addButtonFX;

  @FXML
  private Button shutdownButtonFX;

  @FXML
  private BorderPane pane;

  @FXML
  private VBox vbox;

  @FXML
  private Label meldungNameLbl;

  @FXML
  private ImageView stonewall;

  @FXML
  private ImageView stonewallMirror;

  @FXML
  private Label soundLbl;

  private FxGameButton startButton;
  private FxGameButton addButton;
  private FxGameButton shutdownButton;
  private HandsManager handsManager;
  private Player player;
  private final ScreenName ID = ScreenName.START_SCREEN;

  @FXML
  protected void initialize() {
    this.handsManager = HandsManager.getInstance();
    player = Player.getInstance();
    this.handsManager.register(this);
    changeSizeOfStonewall();
  }

  /**
   * Is called when the hands have been moved and handles intersections with the
   * buttons.
   * 
   * @param leftHand  the left hand
   * @param rightHand the right hand
   */
  @Override
  public void handsMoved(Hand leftHand, Hand rightHand) {
    if (startButton == null || startButton.getX() == 0) {
      startButton = new FxGameButton(gameStartButtonFX, ColorConstants.GREEN);
      addButton = new FxGameButton(addButtonFX, ColorConstants.BLUE);
      shutdownButton = new FxGameButton(shutdownButtonFX, ColorConstants.RED);
    }
    handleIntersection(startButton);
    handleIntersection(addButton);
    handleIntersection(shutdownButton);
  }

  @Override
  public ScreenName getID() {
    return ID;
  }

  /**
   * Gets called when the setOnAction Event of the add-exercise button is invoked,
   * displays a file explorer to choose the new exercise csv.
   * 
   * @param event
   */
  @FXML
  protected void addBtn(ActionEvent event) {
    var fileChooser = new FileChooser();
    var csvIo = new ExerciseCsvIO();
    var exercisesFile = fileChooser.showOpenDialog(MoveMathics.getStage());
    if (exercisesFile != null) {
      var fileWriteOk = csvIo.addCustomExercises(exercisesFile.toPath());

      if (!fileWriteOk) {
        System.out.println("Could not write file.");
      }
    }
  }

  /**
   * Gets called when the setOnAction Event of the shutdown game button is
   * invoked. It quits the game and shuts down the system.
   * 
   * @param event
   */
  @FXML
  protected void shutdownBtn(ActionEvent event) throws IOException {
    MoveMathics.shutdownSystem();
  }

  /**
   * Gets called when the setOnAction Event of the start game button is invoked,
   * checks if a name was typed and switches to the calibrationscreen.
   * 
   * @param event
   */
  @FXML
  protected void startBtn(ActionEvent event) {
    if (!nameTxt.getText().isBlank() && !nameTxt.getText().contains(",")) {
      player.setName(nameTxt.getText());
      ScenesManager.getInstance().switchScene(ScreenName.CALIBRATION_SCREEN);
    } else {
      meldungNameLbl.setVisible(true);
    }
  }

  private void handleIntersection(FxGameButton button) {
    if (this.handsManager.oneHandIntersects(button) != null) {
      button.fireEventWithAnimation();
    }
  }

  private void changeSizeOfStonewall() {
    double width = MoveMathics.getStageWidth() / 2;
    double height = MoveMathics.getStageHeight() / 3.5;
    stonewall.setFitHeight(height);
    stonewall.setFitWidth(width);
    stonewallMirror.setFitHeight(height);
    stonewallMirror.setFitWidth(width);
    stonewall.setViewOrder(100001);
    stonewallMirror.setViewOrder(100000);
  }
}
