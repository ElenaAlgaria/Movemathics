package ch.fhnw.ip12.gui.controller;

import java.util.Arrays;
import java.util.stream.Stream;

import ch.fhnw.ip12.MoveMathics;
import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.FxGameButton;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.Stone;
import ch.fhnw.ip12.gui.gameobject.animation.ColorConstants;
import ch.fhnw.ip12.gui.input.HandControllable;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.manager.SolutionManager;
import ch.fhnw.ip12.gui.manager.SoundManager;
import ch.fhnw.ip12.gui.manager.visibleevent.VisibleEvent;
import ch.fhnw.ip12.gui.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Controller class of scorelist scene: Shows the score of the last game
 */
public class RepetitionController implements HandControllable {
  private FxGameButton showScoreBoardButton;
  private static final ScreenName ID = ScreenName.REPETITION_SCREEN;

  @FXML
  private Pane pane;

  @FXML
  private Button showScoreBoardButtonFX;

  @FXML
  private ImageView steinmauer;

  @FXML
  private ImageView steinmauerMirror;

  @FXML
  private Label questionLabel1;

  @FXML
  private Label questionLabel2;

  @FXML
  private Label questionLabel3;

  @FXML
  private Label correctAnswer1;

  @FXML
  private Label correctAnswer2;

  @FXML
  private Label correctAnswer3;

  @FXML
  private Label selectedAnswer1;

  @FXML
  private Label selectedAnswer2;

  @FXML
  private Label selectedAnswer3;

  @FXML
  private Label punkteLbl;

  @FXML
  private Label numWrongAnwsersLbl;

  @FXML
  private Rectangle rec;

  private Label[] questionLabels;
  private Label[] correctAnswerLabels;
  private Label[] selectedAnswerLabels;

  /**
   * Initializes the controller, this includes: all controls, register at the
   * handsmanger, update the stonewall size and display the wrong exercises.
   */
  @FXML
  public void initialize() {
    HandsManager.getInstance().register(this);
    questionLabels = new Label[] { questionLabel1, questionLabel2, questionLabel3 };
    correctAnswerLabels = new Label[] { correctAnswer1, correctAnswer2, correctAnswer3 };
    selectedAnswerLabels = new Label[] { selectedAnswer1, selectedAnswer2, selectedAnswer3 };
    changeSizeOfStonewall();
    reactToSetVisibleEvents();
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
    if (showScoreBoardButton == null || showScoreBoardButton.getX() == 0) {
      showScoreBoardButton = new FxGameButton(showScoreBoardButtonFX, ColorConstants.BLUE);
    }
    handleIntersection(leftHand, rightHand);
  }

  @Override
  public ScreenName getID() {
    return ID;
  }

  /**
   * Gets called when the setOnAction Event of the showscore button is invoked and
   * switches to the calibrationscreen.
   * 
   * @param event
   */
  @FXML
  protected void showScoreBtn(ActionEvent event) {
    SoundManager.SELECT_BUTTON.getMusic();
    ScenesManager.getInstance().switchScene(ScreenName.SCOREBOARD_SCREEN);
  }

  /**
   * Reads the wrong solved exercises from SolutionManager and displays the result
   * to its labels.
   */
  private void reactToSetVisibleEvents() {
    pane.addEventHandler(VisibleEvent.CUSTOM_EVENT_TYPE, event -> {
      clearWrongExercises();
      punkteLbl.setText(String.valueOf(Player.getInstance().getPoints()));
      numWrongAnwsersLbl.setText(String.valueOf(SolutionManager.getInstance().getData().size()));
      rec.setLayoutX(MoveMathics.getStageWidth() / 2 - 200);
      rec.setLayoutY(MoveMathics.getStageHeight() / 2 - 225);
      displayResults();
      SolutionManager.getInstance().resetWrongExercises();
    });
  }

  private void clearWrongExercises() {
    Stream.of(questionLabels, correctAnswerLabels).flatMap(Stream::of).forEach(l -> l.setText(""));
    Stream.of(selectedAnswerLabels).forEach(l -> l.setText(""));
  }

  private void displayResults() {
    SolutionManager solutionManager = SolutionManager.getInstance();
    for (int i = 0; i < solutionManager.getData().size(); i++) {
      String question = solutionManager.getData().get(i).getQuestion();
      String correctAnswer = Arrays.stream(solutionManager.getData().get(i).getExercise()).filter(Stone::isCorrect)
          .findFirst().map(Stone::getText).orElse(null);
      String selectedAnswer = Arrays.stream(solutionManager.getData().get(i).getExercise()).filter(Stone::isSelected)
          .findFirst().map(Stone::getText).orElse(null);

      selectedAnswerLabels[i].setText(selectedAnswer);

      questionLabels[i].setText(question + " = ");

      correctAnswerLabels[i].setText(" -> " + correctAnswer);

    }
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
   * Handles the intersections with the scoreboard button buttons.
   */
  private void handleIntersection(Hand leftHand, Hand rightHand) {
    if (leftHand.intersects(showScoreBoardButton) != null || rightHand.intersects(showScoreBoardButton) != null) {
      showScoreBoardButton.fireEventWithAnimation();
    }
  }
}
