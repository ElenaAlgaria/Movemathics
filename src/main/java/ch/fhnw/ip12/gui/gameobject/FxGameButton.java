package ch.fhnw.ip12.gui.gameobject;

import ch.fhnw.ip12.gui.gameobject.animation.ButtonAnimation;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/**
 * Class description: Wrapper class for GUI buttons
 */

public class FxGameButton extends GameObject {

  private Button btn;
  private ButtonAnimation btnAnimation = null;

  /**
   * the default color for the animation is used
   * 
   * @param btn The button to wrap
   */
  public FxGameButton(Button btn) {
    super(getXFromBtn(btn), getYFromBtn(btn), (int) btn.getPrefWidth(), (int) btn.getPrefHeight());
    this.btn = btn;
    this.btnAnimation = new ButtonAnimation(this);
  }

  /**
   * 
   * @param btn               The button to wrap
   * @param btnAnimationColor the color of the animation
   */
  public FxGameButton(Button btn, Color btnAnimationColor) {
    super(getXFromBtn(btn), getYFromBtn(btn), (int) btn.getPrefWidth(), (int) btn.getPrefHeight());
    this.btn = btn;
    this.btnAnimation = new ButtonAnimation(this, btnAnimationColor);
  }

  /**
   * fires button event without animation.
   */
  public void fire() {
    btn.fire();
  }

  /**
   * Fires the buttons default javafx event. The event is delayed by
   * BUTTON_EXECUTION_TIME
   */
  public void fireEventWithAnimation() {
    if (!btnAnimation.getAnimationIsRunning()) {
      btnAnimation.start();
    }
  }

  /**
   * abort the animation and resets the animation time
   */
  public void stopFireEventWithAnimation(){
    if (btnAnimation.getAnimationIsRunning()) {
      btnAnimation.stopAnimation();
    }
  }

  /**
   * extracts the y coordinate of the Javafx Button relative to the Window
   */
  private static int getYFromBtn(Button btn) {
    return (int) btn.localToScene(btn.getBoundsInLocal()).getMinY();
  }

  /**
   * extracts the x coordinate of the Javafx Button relative to the Window
   */
  private static int getXFromBtn(Button btn) {
    return (int) btn.localToScene(btn.getBoundsInLocal()).getMinX();
  }

  /**
   * 
   * @return The instance of the wrapped javafx button
   */
  public Button getBtn() {
    return btn;
  }
}
