package ch.fhnw.ip12.gui.manager;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ip12.gui.gameobject.Exercise;

/**
 * carries a list of wrong solved exercises.
 */
public class SolutionManager {

  private List<Exercise> data = new ArrayList<>();
  private static final SolutionManager instance = new SolutionManager();

  private SolutionManager() {
  }

  public static SolutionManager getInstance() {
    return instance;
  }

  /**
   * resets the list of the wrong exercises
   */
  public void resetWrongExercises() {
    data = new ArrayList<>();
  }

  /**
   * @return the exericses
   */
  public List<Exercise> getData() {
    return data;
  }

}
