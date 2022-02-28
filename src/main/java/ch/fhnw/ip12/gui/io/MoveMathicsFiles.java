package ch.fhnw.ip12.gui.io;

public enum MoveMathicsFiles {
  CSV("exercises.csv"), SCOREBOARD("scoreboard.csv");

  private String fileName;

  private MoveMathicsFiles(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }
}
