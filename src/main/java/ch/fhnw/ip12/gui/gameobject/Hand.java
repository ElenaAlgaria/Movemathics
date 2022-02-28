package ch.fhnw.ip12.gui.gameobject;

import org.opencv.core.Point;

import ch.fhnw.ip12.constants.ScreenName;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Class description: a hand displayed on the GUI
 */

public class Hand extends GameObject {
  private static final int HEIGHT = 73;
  private static final int WIDTH = 53;

  private String imagePath;
  private GraphicsContext graphicsContextDoDrawHands;
  private int activeX;
  private int activeY;

  /**
   * constructs a new hand.
   * 
   * @param imagePath the image to display
   * @param x         the default x coordinate
   * @param y         the default y coordinate
   */
  public Hand(String imagePath, int x, int y) {
    super(x, y, WIDTH, HEIGHT);
    this.imagePath = imagePath;

    graphicsContextDoDrawHands = initializeGraphicsContext();
  }

  /**
   * updates the position of the hand.
   * 
   * @param pos the new position to use.
   */
  public void setPosition(Point pos) {
    this.setX((int) pos.x);
    this.setY((int) pos.y);
    this.setHandPosition(getX(), getY());
  }

  @Override
  public void draw(Pane pane) {
    addCanvasToPane(pane);
  }

  private GraphicsContext initializeGraphicsContext() {
    final Canvas canvas = new Canvas(ScreenName.GAME_SCREEN.getScreenWidth(), ScreenName.GAME_SCREEN.getScreenHeight());
    return canvas.getGraphicsContext2D();
  }

  private void addCanvasToPane(Pane pane) {
    pane.getChildren().add(graphicsContextDoDrawHands.getCanvas());
  }

  private Image getHandsImage() {
    return new Image(getClass().getResourceAsStream(imagePath), getWidth(), getHeight(), false, false);
  }

  /**
   * @param x
   * @param y
   */
  private void setHandPosition(double x, double y) {
    drawImageToCanvas(x, y);
  }

  private void drawImageToCanvas(double x, double y) {
    graphicsContextDoDrawHands.clearRect(activeX, activeY, getWidth(), getHeight());
    graphicsContextDoDrawHands.drawImage(getHandsImage(), x, y);
    activeX = getX();
    activeY = getY();
  }
}
