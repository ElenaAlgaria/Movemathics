package ch.fhnw.ip12.gui.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExerciseCsvIO extends MoveMathicsCsvIO {
  private static final String DEFAULT_EXERCISES_FILE_PATH = "/Rechensatz1.csv";

  /**
   * Reads an existing exercise. If the exercise is not existing at the default
   * path, the default exercises are used.
   * 
   * @return all lines of the file
   */
  public List<String> readStoredExercises(MoveMathicsFiles file) {
    if (!fileExists(file)) {
      return readDefaultExercises();
    }
    return readExercisesFromFile(Path.of(file.getFileName()));
  }

  /**
   * reads and saves a new set of exercises.
   * 
   * @param file the path to read from
   * @return true if it worked
   */
  public boolean addCustomExercises(Path file) {
    var fileContent = readExercisesFromFile(file);
    return writeFile(MoveMathicsFiles.CSV, fileContent);
  }

  /**
   * reads exercices from any file
   * 
   * @param file the file to read from
   * @return
   */
  private List<String> readExercisesFromFile(Path file) {
    Path fullPath = MOVE_MATHICS_HOME.resolve(file);
    try {
      return readFromInputStream(new FileReader(fullPath.toFile()));
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    return new ArrayList<>();
  }

  /**
   * reads exercises from default csv in .jar
   * 
   * @return
   */
  private List<String> readDefaultExercises() {
    var existingExerciseFile = new InputStreamReader(getClass().getResourceAsStream(DEFAULT_EXERCISES_FILE_PATH));
    return readFromInputStream(existingExerciseFile);
  }
}
