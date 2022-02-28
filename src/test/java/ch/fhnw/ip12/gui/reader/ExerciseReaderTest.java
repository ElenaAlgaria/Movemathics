
package ch.fhnw.ip12.gui.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import ch.fhnw.ip12.gui.gameobject.Exercise;

public class ExerciseReaderTest {

  @Test
  public void csvReader() {
    ExerciseReader reader = ExerciseReader.getInstance();
    try {
      reader.createExercisesFromSource();
    } catch (Exception ex) {
      fail("exception occured" + ex);
    }
  }

  @Test
  public void nextExercise() {
    ExerciseReader reader = ExerciseReader.getInstance();
    try {
      reader.createExercisesFromSource();
    } catch (Exception ex) {
      fail("exception occured" + ex);
    }
    Exercise aufgabe1 = reader.nextExercise();
    assertNotNull(aufgabe1);
    Exercise aufgabe2 = reader.nextExercise();
    assertNotNull(aufgabe2);
    Exercise aufgabe3 = reader.nextExercise();
    assertNotNull(aufgabe3);
  }
}
