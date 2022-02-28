package ch.fhnw.ip12.gui.gameobject.animation;

import java.util.List;

import ch.fhnw.ip12.MoveMathics;
import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.FxGameButton;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.SoundManager;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class represents a button animation, displayed as soon as a hand is on
 * top of a javafx button. it is referenced by the FxGameButton class.
 */
public class ButtonAnimation {
  private static final long DEFAULT_ANIMATION_TIME = 2_000_000_000L;
  private AnimationTimer timer;
  private boolean animationsIsRunning;
  private Canvas canvasToDrawAnimation;
  private GraphicsContext animationGraphicsContext;
  private FxGameButton fxGameBtn;

  private Color color;

  /**
   * constructs a new button animation.
   * 
   * @param btn   The button to create the atnimation for
   * @param color the color of the animation, use a color defined in
   *              ColorConstants.
   */
  public ButtonAnimation(FxGameButton btn, Color color) {
    this.fxGameBtn = btn;
    this.color = color;
    createCanvasForBtnAnimation();
  }

  /**
   * constructs a new button animation in the default color.
   * 
   * @param btn The button to create the atnimation for
   */
  public ButtonAnimation(FxGameButton btn) {
    this(btn, ColorConstants.GREY);
  }

  /**
   * @return whether the animation is running.
   */
  public boolean getAnimationIsRunning() {
    return animationsIsRunning;
  }

  /**
   * Starts the button animation
   */
  public void start() {
    animationsIsRunning = true;

    timer = createAnimationTimer();
    timer.start();
  }

  /**
   * Stops the animationtimer and cleans up the canvas
   */
  public void stopAnimation() {
    animationsIsRunning = false;
    timer.stop();
  }

  /**
   * Creates an animationTimer to animate the button
   * 
   * @return the created animationTimer
   */
  private AnimationTimer createAnimationTimer() {
    return new AnimationTimer() {
      private long startTime;

      @Override
      public void handle(long now) {
        if (startTime == 0) {
          startTime = now;
        }

        long animationDuration = now - startTime;
        nextButtonAnimationStep(animationDuration);
        checkIfHandStillIntersectsButton();
        fireEventIfReady(animationDuration);

        if(!animationsIsRunning){
          clearGraphicContext();
          removeCanvasFromPane();
        }
      }
    };
  }

  /**
   * Fires the event if the passed parameter is bigger than the buttonanimation time
   * 
   * @param animationDuration the time of the animation until now
   */
  private void fireEventIfReady(long animationDuration) {
    if (animationDuration >= DEFAULT_ANIMATION_TIME) {
      fxGameBtn.getBtn().fire();
      stopAnimation();
      SoundManager.SELECT_BUTTON.getMusic();
    }
  }

  /**
   * Checks whether one hand is still intersecting the button. If not, the animation will be stopped
   */
  private void checkIfHandStillIntersectsButton() {
    if (HandsManager.getInstance().oneHandIntersects(fxGameBtn) == null) {
      stopAnimation();
    }
  }

  /**
   * Clears all element from the canvas
   */
  private void clearGraphicContext() {
    if (animationGraphicsContext != null) {
      animationGraphicsContext.clearRect(0, 0, canvasToDrawAnimation.getWidth(),
          canvasToDrawAnimation.getHeight());
    }
  }

  /**
   * Removes the canvas from the scene.
   */
  private void removeCanvasFromPane() {
    List<Node> children = getPane().getChildren();
    if (children.contains(canvasToDrawAnimation)) {
      children.remove(canvasToDrawAnimation);
    }
  }

  /**
   * Draws (using canvas) a circle behind the button. With every time the method is called, the
   * circle is being increased.
   */
  private void nextButtonAnimationStep(long animationDuration) {
    double angle = calculateNextAngleRad(animationDuration);
    double radius = fxGameBtn.getBtn().getHeight() / 2;
    animationGraphicsContext.setFill(color);
    animationGraphicsContext.fillOval(calculateNextX(angle, radius) - 12,
        calculateNextY(angle, radius) - 12, 24, 24);
    addCanvasToPane();
  }

  /**
   * Calculates an x coordinate of a point according to a given radius and angle
   * 
   * @param angle the angle fo the x coordinate
   * @param radius the radius of the circle
   * @return the x coordinate of the point
   */
  private double calculateNextX(double angle, double radius) {
    double center = this.fxGameBtn.getX() + radius;
    return center + radius * Math.cos(angle);
  }

  /**
   * Calculates an y coordinate of a point according to a given radius and angle
   * 
   * @param angle the angle fo the y coordinate
   * @param radius the radius of the circle
   * @return the y coordinate of the point
   */
  private double calculateNextY(double angle, double radius) {
    double center = this.fxGameBtn.getY() + radius;
    return center + radius * Math.sin(angle);
  }

  /**
   * Adds the canvas with the animation on it to the pane, and sends it to back.
   */
  private void addCanvasToPane() {
    removeCanvasFromPane();
    ObservableList<Node> children = getPane().getChildren();
    children.add(canvasToDrawAnimation);
    canvasToDrawAnimation.setViewOrder(1); // sends Canvas to back
  }

  /**
   * Get the pane of the scene
   * 
   * @return javafx Pane
   */
  private Pane getPane() {
    return (Pane) MoveMathics.getStage().getScene().getRoot();
  }

  /**
   * Caluclates the next angle for the button animation
   * 
   * @return the angle
   */
  private double calculateNextAngleRad(long animationDuration) {
    double x = DEFAULT_ANIMATION_TIME / 100_000f;
    double y = animationDuration / 100_000f;
    return Math.toRadians(360 / x * y);
  }

  /**
   * Creates a new cavas and a new graphiccontext. On the graphiccontext you can create shapes.
   */
  private void createCanvasForBtnAnimation() {
    canvasToDrawAnimation = new Canvas(ScreenName.CALIBRATION_SCREEN.getScreenWidth(),
        ScreenName.CALIBRATION_SCREEN.getScreenHeight());
    animationGraphicsContext = canvasToDrawAnimation.getGraphicsContext2D();
  }
}
