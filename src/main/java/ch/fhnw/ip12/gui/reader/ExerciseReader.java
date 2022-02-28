package ch.fhnw.ip12.gui.reader;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.Exercise;
import ch.fhnw.ip12.gui.gameobject.Stone;
import ch.fhnw.ip12.gui.io.ExerciseCsvIO;
import ch.fhnw.ip12.gui.io.MoveMathicsFiles;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import javafx.scene.layout.Pane;

/**
 * Class description: Singleton class to read data from the csv file to create
 * exercises
 * 
 */
public class ExerciseReader {
  private static Logger exerciseReaderLogger = LogManager.getLogger(ExerciseReader.class);
  private static final ExerciseReader INSTANCE = new ExerciseReader();
  private static final String EXERCISE_STONE = "/images/stein1.png";
  private int width = ScreenName.GAME_SCREEN.getScreenWidth();
  private Exercise[] exercises;
  private int nr = 0;
  private int indexCorrectValue;
  private String correctValue;

  private ExerciseReader() {
    exerciseReaderLogger.traceEntry("Ctor");
  }

  /**
   * @return an instance of the exercise reader
   */
  public static ExerciseReader getInstance() {
    return INSTANCE;
  }

  /**
   * creates the exercises from the movemathics default source
   * 
   */
  public void createExercisesFromSource() {
    var csvIo = new ExerciseCsvIO();
    List<String> csvLines = csvIo.readStoredExercises(MoveMathicsFiles.CSV);
    this.createExercisesFromSource(csvLines);
  }

  /**
   * creates the exercises from the defined source
   * 
   * @param csvLines The lines of a csv file with comma separated values
   */
  public void createExercisesFromSource(List<String> csvLines) {
    String[] values;

    exercises = new Exercise[12];
    int count = 0;
    for (String line : csvLines) {
      values = line.split(",");
      // create stones with text from csv file
      String firstValue = values[0];
      correctValue = values[1];

      Stone[] stones = new Stone[3];
      List<String> valueList = new LinkedList<>(Arrays.asList(values));

      valueList.remove(0);
      Collections.shuffle(valueList);

      for (int i = 0; i < valueList.size(); i++) {
        if (valueList.get(i).equals(correctValue)) {
          indexCorrectValue = i;
        }
      }
      stones[0] = new Stone(EXERCISE_STONE, width / 4 - 50, 0, valueList.get(0), indexCorrectValue == 0);
      stones[1] = new Stone(EXERCISE_STONE, width * 2 / 4 - 90, 0, valueList.get(1), indexCorrectValue == 1);
      stones[2] = new Stone(EXERCISE_STONE, width * 3 / 4 - 120, 0, valueList.get(2), indexCorrectValue == 2);

      // create "aufgabe" with 3 stones
      Exercise q = new Exercise(width / 2, 20, count, firstValue, stones);
      exercises[count] = q;
      count++;
    }
  }

  /**
   * @return sets the active exercise to the next exercise and returns it.
   */
  public Exercise nextExercise() {
    // nr 20 if game restarted
    nr++;

    if (nr < exercises.length) {
      activeExercise().getQuestion();
      return exercises[nr];
    } else {
      ScenesManager.getInstance().switchScene(ScreenName.SCOREBOARD_SCREEN);
      nr = 0;
      return exercises[exercises.length - 1];
    }
  }

  /**
   * @return the correct value of the exercise
   */
  public String getCorrectValue() {
    return correctValue;
  }

  /**
   * @return how many exercises do exist
   */
  public int getLength() {
    return exercises.length;
  }

  /**
   * @return the number of the acitve exercise
   */
  public int getNr() {
    return nr;
  }

  /**
   * Resets exercises
   */
  public void resetExercises(Pane pane) {
    if (exercises != null) {
      for (Exercise e : exercises) {
        e.removeExercise(pane);
      }
    }
    createExercisesFromSource();
    nr = 0;
  }

  /**
   * @return the active displayed exercise on the screen.
   */
  public Exercise activeExercise() {
    if (nr >= exercises.length) {
      return exercises[exercises.length - 1];
    }
    return exercises[nr];
  }

  /**
   * If the last exercise is displayed.
   * 
   * @return
   */
  public boolean isEndOfExercises() {
    return nr == exercises.length - 1;
  }

  static int[] randomInts() {
    int[] ar = { 1, 2, 3 };
    Random rnd = ThreadLocalRandom.current();
    for (int i = ar.length - 1; i > 0; i--) {
      int index = rnd.nextInt(i + 1);
      int a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
    return ar;
  }
}
