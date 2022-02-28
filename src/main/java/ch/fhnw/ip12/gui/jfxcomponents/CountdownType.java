package ch.fhnw.ip12.gui.jfxcomponents;

import ch.fhnw.ip12.gui.gameobject.animation.ColorConstants;
import javafx.scene.paint.Color;

/**
 * Types of the countdown.
 */
public enum CountdownType {
  START("/images/countdown-bg/countdown-bg-green.png", ColorConstants.GREEN, "Das Spiel startet in:"),
  CORRECT("/images/countdown-bg/countdown-bg-green.png", ColorConstants.GREEN, "Nächste Aufgabe in:"),
  INCORRECT("/images/countdown-bg/countdown-bg-red.png", ColorConstants.RED, "Nächste Aufgabe in:"),
  FINISH("/images/countdown-bg/countdown-bg-gold.png", ColorConstants.GOLD, "Super, alles richtig gemacht!"),
  GAME_OVER("/images/countdown-bg/countdown-bg-blue.png", ColorConstants.BLUE, "Das Spiel ist vorbei"),
  AFTER_BREAK("/images/countdown-bg/countdown-bg-blue.png", ColorConstants.BLUE, "Das Spiel geht weiter in:");

  private String imagePath;
  private Color color;
  private String message;

  /**
   * Constrcuts a new countdown type
   * 
   * @param imagePath the path to the bacground of the countdown.
   * @param color     the color for the text.
   * @param message   the text for the heading.
   */
  private CountdownType(String imagePath, Color color, String message) {
    this.imagePath = imagePath;
    this.color = color;
    this.message = message;
  }

  public String getImage() {
    return imagePath;
  }

  public Color getColor() {
    return color;
  }

  public String getMessage() {
    return message;
  }
}
