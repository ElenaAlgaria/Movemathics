package ch.fhnw.ip12.gui.controller;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;

import ch.fhnw.ip12.MoveMathics;
import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.FxGameButton;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.animation.ColorConstants;
import ch.fhnw.ip12.gui.input.HandControllable;
import ch.fhnw.ip12.gui.io.MoveMathicsFiles;
import ch.fhnw.ip12.gui.io.ScoreboardCsvIO;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.manager.visibleevent.VisibleEvent;
import ch.fhnw.ip12.gui.model.Player;
import ch.fhnw.ip12.gui.model.Score;
import ch.fhnw.ip12.gui.reader.ExerciseReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Controller class of scoreboard scene: Shows the list of the best players
 */
public class ScoreboardController implements HandControllable {
  private static final ScreenName ID = ScreenName.SCOREBOARD_SCREEN;
  private static final String PATH_SCOREBOARD = "src/main/resources/Scoreboard.csv";
  private ScenesManager scenesManager;
  private FxGameButton restartButton;
  private FxGameButton homeButton;
  private FxGameButton scoreBoardUpButton;
  private FxGameButton scoreBoardDownButton;
  private FxGameButton scoreBoardReset;
  private HandsManager handsManager;
  private int scrollIndex = 0;
  private ScoreboardCsvIO csvIo = new ScoreboardCsvIO();

  @FXML
  private Pane pane;

  @FXML
  private Button restartGameButtonFX;

  @FXML
  private Button homeGameButtonFX;

  @FXML
  private Button scoreBoardUpButtonFX;

  @FXML
  private Button scoreBoardDownButtonFX;

  @FXML
  private Button scoreBoardResetFX;

  @FXML
  private ImageView steinmauer;

  @FXML
  private ImageView steinmauerMirror;

  @FXML
  private TableView scoreRang;

  @FXML
  private TableColumn rang;

  @FXML
  private TableColumn spielername;

  @FXML
  private TableColumn punkte;

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
      restartButton = new FxGameButton(restartGameButtonFX, ColorConstants.GREEN);
      homeButton = new FxGameButton(homeGameButtonFX, ColorConstants.GREEN);
      scoreBoardUpButton = new FxGameButton(scoreBoardUpButtonFX, ColorConstants.BLUE);
      scoreBoardDownButton = new FxGameButton(scoreBoardDownButtonFX, ColorConstants.BLUE);
      scoreBoardReset = new FxGameButton(scoreBoardResetFX, ColorConstants.RED);
    }
    handleIntersection(restartButton);
    handleIntersectionNormalEvent(scoreBoardUpButton);
    handleIntersectionNormalEvent(scoreBoardDownButton);
    handleIntersection(scoreBoardReset);
    handleIntersection(homeButton);
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
    scenesManager = ScenesManager.getInstance();
    handsManager = HandsManager.getInstance();
    handsManager.register(this);
    changeSizeOfStonewall();
    reactToSetVisibleEvents();
  }

  /**
   * Gets called when the setOnAction Event of the restart game button is invoked
   * and switches to the calibrationscreen.
   * 
   * @param event
   */
  @FXML
  protected void restartGameBtn(ActionEvent event) {
    scenesManager.switchScene(ScreenName.CALIBRATION_SCREEN);
  }

  /**
   * Gets called when the setOnAction Event of the home game button is invoked and
   * switches to the startscreen.
   * 
   * @param event
   */
  @FXML
  protected void homeGameBtn(ActionEvent event) {
    scenesManager.switchScene(ScreenName.START_SCREEN);
  }

  /**
   *
   * Move scoreboard up stops if start is reached
   * 
   * @param event
   */
  @FXML
  protected void scoreboardUpBtn(ActionEvent event) {
    if (scrollIndex != 0) {
      scrollIndex -= 1;
    }
    scoreRang.scrollTo(scrollIndex);
  }

  /**
   * Move scoreboard down stops if end is reached
   * 
   * @param event
   */
  @FXML
  protected void scoreboardDownBtn(ActionEvent event) {
    if (scoreRang.getItems().size() - 8 != scrollIndex) {
      scrollIndex += 1;
    }
    scoreRang.scrollTo(scrollIndex);
  }

  private void reactToSetVisibleEvents() {
    pane.addEventHandler(VisibleEvent.CUSTOM_EVENT_TYPE, event -> {
      loadScoreboard();
    });
  }

  /**
   *
   * @param button at which a event will fired
   */
  private void handleIntersectionNormalEvent(FxGameButton button) {
    if (handsManager.oneHandIntersects(button) != null) {
      button.fire();
    }
  }

  private void handleIntersection(FxGameButton button) {
    if (handsManager.oneHandIntersects(button) != null) {
      button.fireEventWithAnimation();
    }
  }

  /**
   * Delete the file "Scoreboard.csv" and create a new one Reload the TableView
   * with the empty File
   */
  @FXML
  private void resetScoreboard() {
    csvIo.recreateFile(MoveMathicsFiles.SCOREBOARD);
    createTable();
  }

  /**
   * Write Score to CSV: filename,points
   *
   */
  private void fileWriter() {
    csvIo.appendLine(MoveMathicsFiles.SCOREBOARD,
        Player.getInstance().getName() + "," + Player.getInstance().getPoints());
  }

  /**
   * Creates the tableview on scoreboard with its entries.
   * 
   */
  private void createTable() {
    ObservableList<Score> data = FXCollections.observableArrayList();
    for (String line : csvIo.readFile(MoveMathicsFiles.SCOREBOARD)) {
      if (!line.isEmpty()) {
        String[] values = line.split(",");
        data.add(new Score(values[0], Integer.parseInt(values[1].trim())));
      }
    }

    Collections.sort(data, Comparator.comparing(Score::getScore).reversed());

    scoreRang.setItems(data);

    punkte.setStyle("-fx-alignment: center; -fx-text-fill: black");
    spielername.setStyle("-fx-alignment: center; -fx-text-fill: black");
  }

  /**
   * Connection between class Score and the table view column
   */
  private void coulmnConnection() {
    // Connect column to the the class ScoreboardManager and its variables
    spielername.setCellValueFactory(new PropertyValueFactory<Score, String>("name"));
    punkte.setCellValueFactory(new PropertyValueFactory<Score, Integer>("score"));
  }

  /**
   *
   * Method to load the Scoreboard from the Scoreboard.csv into the table view
   *
   * @throws IOException Scoreboard.csv file not found
   */
  private void loadScoreboard() {

    fileWriter();

    coulmnConnection();

    createTable();

    scoreRang.scrollTo(0);

    Label tablePlaceHolder = new Label("Keine Daten vorhanden.");
    tablePlaceHolder.setStyle(" -fx-text-fill: black");
    scoreRang.setPlaceholder(tablePlaceHolder);

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
}
