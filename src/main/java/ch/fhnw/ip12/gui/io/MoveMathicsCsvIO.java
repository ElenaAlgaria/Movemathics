package ch.fhnw.ip12.gui.io;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MoveMathicsCsvIO {
  private static final String MOVE_MATHICS_HOME_NAME = ".movemathics";
  protected static final Path MOVE_MATHICS_HOME = Paths.get(System.getProperty("user.home"), MOVE_MATHICS_HOME_NAME);

  /**
   * Writes given data to a CSV file.
   * 
   * @param file              the file to write the data to
   * @param dataToWriteToFile the data to write into the file
   * @return if filewrite was successful, true otherwise false
   */
  public boolean writeFile(MoveMathicsFiles file, List<String> dataToWriteToFile) {
    Path fullPath = MOVE_MATHICS_HOME.resolve(file.getFileName());
    if (!Files.exists(fullPath)) {
      createFile(MOVE_MATHICS_HOME, file.getFileName());
    }
    try (FileWriter output = new FileWriter(fullPath.toFile(), false)) {
      for (String s : dataToWriteToFile) {
        output.write(s + "\n");
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * deletes a movemathics file
   * 
   * @param file the file to delete
   * @return true if it was deleted, else false
   */
  public boolean deleteFile(MoveMathicsFiles file) {
    try {
      return Files.deleteIfExists(MOVE_MATHICS_HOME.resolve(file.getFileName()));
    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println("Could not delete file.");
      return false;
    }
  }

  /**
   * Checks whether movemathics file exists.
   * 
   * @param file the file to check
   * @return true if it exists, else false
   */
  public boolean fileExists(MoveMathicsFiles file) {
    var fullPath = MOVE_MATHICS_HOME.resolve(file.getFileName());
    return Files.exists(fullPath);
  }

  /**
   * Creates a file and its parent directories
   * 
   * @param directories the directories to create
   * @param fileName    the name of the file that should be created in the
   *                    directories
   */
  protected void createFile(Path directories, String fileName) {
    try {
      Files.createDirectories(directories);
      Files.createFile(directories.resolve(fileName));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  protected List<String> readFromInputStream(InputStreamReader is) {
    List<String> valuesList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(is)) {
      String line = "";
      while ((line = br.readLine()) != null) {
        valuesList.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return valuesList;
  }
}
