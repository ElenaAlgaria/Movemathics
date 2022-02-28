package ch.fhnw.ip12.gui.gameobject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.ip12.gui.gameobject.animation.StoneAnimation;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Class description: an exercise object represents a question and 3 possible
 * answers. Which can be drawn to a pane.
 */

public class Exercise extends GameObject {
  private static Logger exerciseLogger = LogManager.getLogger(Exercise.class);

  private String question;
  private Stone[] stones;
  private StoneAnimation stoneAnimation;

  private boolean stoneIntersectionIsEnabled = true;

  /**
   * Constructs a new Exercise instance
   * 
   * @param x        the x coordinate
   * @param y        the y coordinate
   * @param nr       the number of the exercise
   * @param question the question of the exercise
   * @param exercise the stones with the answers.
   */
  public Exercise(int x, int y, int nr, String question, Stone[] exercise) {
    super(x, y, 100, 10);
    this.question = question;
    this.stones = exercise;
    this.stoneAnimation = new StoneAnimation();
    exerciseLogger.trace("create exercise: {}", question);
  }

  /**
   * @return all available answers as stones.
   */
  public Stone[] getExercise() {
    return stones;
  }

  /**
   * @return the question as string.
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Draws the exercsie on a given pane.
   * 
   * @param pane the pane to draw the exercise to.
   */
  @Override
  public void draw(Pane pane) {
    Label l = new Label(question);
    l.setLayoutX(getX());
    l.setLayoutY(getY());
    pane.getChildren().add(l);
    for (Stone s : stones) {
      s.draw(pane);
    }
  }

  /**
   * @return GameObject
   */
  @Override
  public GameObject intersects(GameObject other) {
    for (Stone stone : stones) {
      if (stone.intersects(other) != null) {
        return stone;
      }
    }
    return null;
  }

  /**
   * Removes the exercise from a given pane.
   * 
   * @param pane the pane to remove the exercise from.
   */
  public void removeExercise(Pane pane) {
    stoneAnimation.stop();
    for (Stone stone : stones) {
      stone.removeStone(pane);
    }
  }

  /**
   * Stops the animation of the stones.
   */
  public void stopAnimation() {
    stoneAnimation.stop();
  }

  /**
   * Starts the antimation of the stones.
   */
  public void startAnimation() {
    stoneAnimation.start(stones);
  }

  /**
   * @return Whether stones can be selected at the moment or not.
   */
  public boolean stoneIntersectionIsEnabled() {
    return stoneIntersectionIsEnabled;
  }

  /**
   * Enable stone intersection
   * 
   * @param enabled
   */
  public void setStoneIntersectionEnabled(boolean enabled) {
    stoneIntersectionIsEnabled = enabled;
  }
}
