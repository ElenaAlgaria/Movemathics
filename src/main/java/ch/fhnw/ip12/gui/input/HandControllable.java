package ch.fhnw.ip12.gui.input;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.Hand;

/**
 * Every class which implements this interface can be registered in the
 * HandsManager.java class to get notified when a hand is moved.
 */
public interface HandControllable {

  /**
   * @return ScreenName the Enum represenation screenname of the active
   *         controller.
   */
  ScreenName getID();

  /**
   * This method is invoked as soon a hand has moved.
   * 
   * @param leftHand  the position of the left hand.
   * @param rightHand the position of the right hand.
   */
  void handsMoved(Hand leftHand, Hand rightHand);
}
