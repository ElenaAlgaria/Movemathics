package ch.fhnw.ip12.gui.gameobject;

import javafx.scene.layout.Pane;

/**
 * Class description: super class for several game objects
 */

public abstract class GameObject {
  private int x;
  private int y;
  private int width;
  private int height;

  protected GameObject(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * updates the x coordinate.
   * 
   * @param x
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * @return int the active x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Updates the x-coordinate.
   * 
   * @param y
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * @return int the active y coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Draws the game object on a given pane.
   * 
   * @param pane the pane to draw the gameobject to.
   */
  public void draw(Pane pane) {
  }

  /**
   * detect collisions of two game objects
   * 
   * @param other
   * @return GameObject
   */
  public GameObject intersects(GameObject other) {
    if (x + width > other.x && y + height > other.y && x < other.x + other.width && y < other.y + other.height) {
      return this;
    }
    return null;
  }

  /**
   * @return String
   */
  @Override
  public String toString() {
    return "GameObject [x=" + x + ", y=" + y + "]";
  }

  /**
   * @return int the atual width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the width of the gameobject.
   * 
   * @param width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * @return int the active height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the height of the gameobject.
   * 
   * @param height
   */
  public void setHeight(int height) {
    this.height = height;
  }

}
