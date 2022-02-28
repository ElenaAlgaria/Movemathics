package ch.fhnw.ip12.gui.controller.gamecontroller.gameflow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.Stone;
import ch.fhnw.ip12.gui.jfxcomponents.CountdownLabel;
import ch.fhnw.ip12.gui.jfxcomponents.CountdownType;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.manager.SolutionManager;
import ch.fhnw.ip12.gui.manager.SoundManager;
import ch.fhnw.ip12.gui.model.Player;
import ch.fhnw.ip12.gui.reader.ExerciseReader;
import ch.fhnw.ip12.mqtt.ScoreSender;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Handles the movemathics gameflow.
 */
public class GameFlow {
  private static Logger gameFlowLogger = LogManager.getLogger(GameFlow.class);

  private static final String CORRECT_STONE = "/images/stone_correct.png";
  private static final String INCORRECT_STONE = "/images/stone_incorrect.png";
  private static final int EXERCISE_NUMBER = 12;

  private Tower tower;
  private StrikeHandler strikeHandler;
  private ExerciseReader exerciseReader;
  private Label scoreLbl;
  private Label exercise;
  private CountdownLabel countdownLabel;
  private Player player;
  private Pane pane;

  /**
   * constructs new Gameflow object
   * 
   * @param pane           the pane to draw all elements
   * @param tower          the tower which is built with every correct answer
   * @param strikeHandler  the instance of the strikeHandler
   * @param scoreLbl       the label to update the score
   * @param execrise       the label of the exercise
   * @param countdownLabel an instance of a countdown label to play the countdowns
   *                       on.
   */
  public GameFlow(Pane pane, Tower tower, StrikeHandler strikeHandler, Label scoreLbl, Label execrise,
      CountdownLabel countdownLabel) {
    this.tower = tower;
    this.strikeHandler = strikeHandler;
    this.scoreLbl = scoreLbl;
    this.exercise = execrise;
    this.pane = pane;
    this.countdownLabel = countdownLabel;
    exerciseReader = ExerciseReader.getInstance();
    player = Player.getInstance();
  }

  /**
   * Is called when the hand position changes.
   * 
   * @param leftHand  with its position in it
   * @param rightHand with its position in it
   */
  public void handleHandPositionChange(Hand leftHand, Hand rightHand) {
    checkIfStoneIntersectsBottomWall(exerciseReader.activeExercise().getExercise());
    checkIfStoneIntersectsHand(leftHand, rightHand);
  }

  /**
   * Can be called, when the game should be continued. It displays a countdown
   * label and afterwards the active exercise.
   */
  public void continueGame() {
    countDown(CountdownType.AFTER_BREAK, displayActiveExercise());
  }

  /**
   * Resets the game and displays a new countdown. The reset includes: tower,
   * strikes, points, exercises
   */
  public void restart() {
    strikeHandler.reset();
    tower.reset();

    player.setPoints(0);
    scoreLbl.setText("Score: " + player.getPoints());

    exerciseReader.resetExercises(pane);
    exercise.setText(exerciseReader.activeExercise().getQuestion());
    gameFlowLogger.debug("restart game");
    countDown(CountdownType.START, () -> {
      exerciseReader.activeExercise().draw(pane);
      exerciseReader.activeExercise().setStoneIntersectionEnabled(true);
      exerciseReader.activeExercise().startAnimation();
    });
  }

  private void checkIfStoneIntersectsHand(Hand leftHand, Hand rightHand) {
    if (exerciseReader.getNr() < EXERCISE_NUMBER && exerciseReader.activeExercise().stoneIntersectionIsEnabled()) {
      Stone intersectedStone = (Stone) exerciseReader.activeExercise().intersects(leftHand);
      if (intersectedStone == null) {
        intersectedStone = (Stone) exerciseReader.activeExercise().intersects(rightHand);
      }
      boolean correctAnswered = false;
      if (intersectedStone != null) {
        correctAnswered = exerciseSolved(intersectedStone);
        handleAnswer(correctAnswered);
      }
    }
  }

  /**
   * @param correctAnswered
   */
  private void nextExercise(boolean correctAnswered) {
    CountdownType cdType = CountdownType.CORRECT;
    if (!correctAnswered) {
      cdType = CountdownType.INCORRECT;
    }
    countDown(cdType, displayNextExerciseRunnable());

  }

  /**
   * @param correctAnswered
   */
  private void handleAnswer(boolean correctAnswered) {
    if (correctAnswered) {
      SoundManager.RIGHT_ANSWER.getMusic();
      incrementScore();
    } else {
      SolutionManager.getInstance().getData().add(exerciseReader.activeExercise());
      SoundManager.WRONG_ANSWER.getMusic();
      strikeHandler.addStrike();
    }

    if (!isGameOver()) {
      nextExercise(correctAnswered);
    } else {
      gameOver();
    }
  }

  private void incrementScore() {
    tower.buildTower();
    player.incPoints();
    scoreLbl.setText("Score: " + player.getPoints());
  }

  /**
   * @param stone
   * @return boolean
   */
  private boolean exerciseSolved(Stone stone) {
    if (!stone.isSelected()) {
      boolean correct = stone.isCorrect();
      updateStonePicture(stone, correct);
      return correct;
    }
    return false;
  }

  /**
   * @return boolean
   */
  private boolean isGameOver() {
    boolean gameOverByStrikes = strikeHandler.isGameOver();
    boolean endOfExecise = exerciseReader.isEndOfExercises();
    return gameOverByStrikes || endOfExecise;
  }

  /**
   * @param stones
   */
  private void checkIfStoneIntersectsBottomWall(Stone[] stones) {
    int screenHeight = ScreenName.GAME_SCREEN.getScreenHeight() - stones[0].getHeight();
    for (Stone stone : stones) {
      if (stone.isCorrect() && stone.getY() >= screenHeight && !stone.isSelected()) {
        updateStonePicture(stone, true);
        handleAnswer(false);
      }
    }
  }

  /**
   * @return Runnable
   */
  private Runnable displayNextExerciseRunnable() {
    return () -> {
      exerciseReader.activeExercise().setStoneIntersectionEnabled(true);
      exerciseReader.activeExercise().removeExercise(pane);
      exerciseReader.nextExercise().draw(pane);
      exerciseReader.activeExercise().startAnimation();
      exercise.setText(exerciseReader.activeExercise().getQuestion());
    };
  }

  /**
   * @return Runnable
   */
  private Runnable displayActiveExercise() {
    return () -> {
      exerciseReader.activeExercise().setStoneIntersectionEnabled(true);
      exerciseReader.activeExercise().startAnimation();
    };
  }

  private void gameOver() {
    if (strikeHandler.getStrikeCount() > 0) {
      SoundManager.GAME_LOST.getMusic();
      countDown(CountdownType.GAME_OVER, () -> ScenesManager.getInstance().switchScene(ScreenName.REPETITION_SCREEN));
    } else {
      SoundManager.GAME_WON.getMusic();
      countDown(CountdownType.FINISH, () -> ScenesManager.getInstance().switchScene(ScreenName.SCOREBOARD_SCREEN));
    }
    strikeHandler.resetStrikeCount();
    exerciseReader.activeExercise().stopAnimation();
    gameFlowLogger.debug("game over - score: {}", player.getPoints());
    ScoreSender.getInstance().sendResults();
  }

  /**
   * @param intersectedStone
   * @param correct
   */
  private void updateStonePicture(Stone intersectedStone, boolean correct) {
    if (correct) {
      intersectedStone.displayDifferentStone(CORRECT_STONE);
    } else {
      intersectedStone.displayDifferentStone(INCORRECT_STONE);
    }
  }

  /**
   * @param message
   * @param color
   * @param runnable
   */
  private void countDown(CountdownType countdownType, Runnable runnable) {
    SoundManager.COUNTDOWN.getMusic();
    exerciseReader.activeExercise().setStoneIntersectionEnabled(false);
    countdownLabel.start(countdownType, runnable);
    exerciseReader.activeExercise().stopAnimation();
    gameFlowLogger.debug("next exercise: {}", countdownType.getMessage());
  }
}
