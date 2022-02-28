package ch.fhnw.ip12.gui.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.opencv.core.Point;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.GameObject;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.input.HandControllable;
import ch.fhnw.ip12.gui.reader.ExerciseReader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Class description: Singleton class to manage the hand position and
 * communicates with the active scene. Scense can implement the HandControllable
 * interface and register itself on the HandsManager to get updates to
 * handmovements.
 */

public class HandsManager {

  private static final HandsManager INSTANCE = new HandsManager();

  private List<HandControllable> movementConsumers;
  private HandControllable activeMovementConsumer;
  private Hand leftHand;
  private Hand rightHand;
  ExerciseReader aufgabenReader;
  Label l;

  /**
   * private constructor to create a Handmanager
   */
  private HandsManager() {
    movementConsumers = new ArrayList<>();

    leftHand = new Hand("/images/left-hand.png", 0, 0);
    rightHand = new Hand("/images/right-hand.png", 0, 0);
  }

  /**
   * @return HandsManager returns the instance of the HandManager
   */
  public static HandsManager getInstance() {
    return INSTANCE;
  }

  /**
   * Can be called to update the hands positions. Is called by the movemathics
   * main class.
   * 
   * @param pointLeftHand  the new point of the left hand
   * @param pointRightHand the new point of the right hand
   */
  public void moveHands(Point pointLeftHand, Point pointRightHand) {
    if (this.leftHand != null && this.rightHand != null) {
      this.leftHand.setPosition(new Point((int) pointLeftHand.x, (int) pointLeftHand.y));
      this.rightHand.setPosition(new Point((int) pointRightHand.x, (int) pointRightHand.y));
    }
    notifyConsumers();
  }

  /**
   * A Handcontrollable can register itself on the handsmanger to get updates of
   * the hand positions
   * 
   * @param hc the handcontrollable to register.
   */
  public void register(HandControllable hc) {
    movementConsumers.add(hc);
  }

  /**
   * Only one consumer at a time can receive updates on the hand position. This
   * method switches the active consumer. it is called when a scene is switched.
   * 
   * @param newScreen the new consumer
   */
  public void changeActiveMovementConsumer(ScreenName newScreen) {
    try {
      activeMovementConsumer = movementConsumers.stream().filter(hc -> hc.getID() == newScreen).findFirst()
          .orElseThrow();
    } catch (NoSuchElementException ex) {
      ScenesManager.getInstance().switchScene(ScreenName.START_SCREEN);
    }
  }

  /**
   * Draws hands to a pane
   * 
   * @param pane
   */
  public void addHands(Pane pane) {
    leftHand.draw(pane);
    rightHand.draw(pane);
  }

  /**
   * Helper method to check whether one hand intersects a given gameobject
   * 
   * @param gameObject the game object to check
   * @return null if no intersection detected else the intersected gameobject
   *         (binary operation)
   */
  public GameObject oneHandIntersects(GameObject gameObject) {
    if (leftHand.intersects(gameObject) != null || rightHand.intersects(gameObject) != null) {
      return gameObject;
    }
    return null;
  }

  /**
   * @return the righthand
   */
  public Hand getRightHand() {
    return rightHand;
  }

  /**
   * @return The lef hand
   */
  public Hand getLeftHand() {
    return leftHand;
  }

  private void notifyConsumers() {
    if (this.activeMovementConsumer != null) {
      this.activeMovementConsumer.handsMoved(leftHand, rightHand);
    }
  }

}
