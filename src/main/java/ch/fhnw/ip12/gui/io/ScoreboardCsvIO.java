package ch.fhnw.ip12.gui.io;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardCsvIO extends MoveMathicsCsvIO {

  /**
   * Reads a given movemathics file
   * 
   * @param filename the file to read
   * @return the lines of the file
   */
  public List<String> readFile(MoveMathicsFiles filename) {
    var fullpath = MOVE_MATHICS_HOME.resolve(filename.getFileName());
    try {
      var fr = new FileReader(fullpath.toFile());
      return super.readFromInputStream(fr);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * deletes and recreates a file
   * 
   * @param file the file to recreate
   */
  public void recreateFile(MoveMathicsFiles file) {
    if (fileExists(file)) {
      deleteFile(file);
      createFile(MOVE_MATHICS_HOME, file.getFileName());
    }
  }

  /**
   * appends a new line to the scoreboard
   * 
   * @param scoreboard
   * @param string
   */
  public void appendLine(MoveMathicsFiles scoreboard, String string) {
    var fullPath = MOVE_MATHICS_HOME.resolve(scoreboard.getFileName());
    if (!fileExists(scoreboard)) {
      try {
        Files.createFile(fullPath);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    try (BufferedWriter output = new BufferedWriter(new FileWriter(fullPath.toFile(), true))) {
      output.write(string + "\n");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
