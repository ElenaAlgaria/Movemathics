package ch.fhnw.ip12.constants;

import ch.fhnw.ip12.MoveMathics;

/**
 * This enum has an entry for every screen with it's path to the fxml file.
 */
public enum ScreenName {

  GAME_SCREEN("/views/Game.fxml"), CALIBRATION_SCREEN("/views/Calibration.fxml"), MENU_SCREEN("/views/Menu.fxml"),
  SCOREBOARD_SCREEN("/views/Scoreboard.fxml"), REPETITION_SCREEN("/views/Repetition.fxml"),
  START_SCREEN("/views/Startscreen.fxml");

  private static final int SCREEN_WIDTH = (int) MoveMathics.getStageWidth();
  private static final int SCREEN_HEIGHT = (int) MoveMathics.getStageHeight();
  private String path;

  private ScreenName(String path) {
    this.path = path;
  }

  public int getScreenWidth() {
    return SCREEN_WIDTH;
  }

  public int getScreenHeight() {
    return SCREEN_HEIGHT;
  }

  public String getPath() {
    return path;
  }
}

