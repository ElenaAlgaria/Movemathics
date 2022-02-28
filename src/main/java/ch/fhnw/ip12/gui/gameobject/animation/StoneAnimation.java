package ch.fhnw.ip12.gui.gameobject.animation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import ch.fhnw.ip12.gui.gameobject.Stone;
import javafx.animation.AnimationTimer;

/**
 * Is used to animate the falling of the stones.
 */
public class StoneAnimation {
  private static final int DURATION = 10_000_000;
  private AnimationTimer timer;
  private boolean isRunning = false;
  private int[] speed = { 1, 2, 3 };
  private long lastUpdate = 0;

  /**
   * constructs a new stone animation.
   */
  public StoneAnimation() {
    shuffleArray(speed);
  }

  /**
   * Start the stone animation for each given stone.
   * 
   * @param stones The stones to animate.
   */
  public void start(Stone[] stones) {
    timer = new AnimationTimer() {

      @Override
      public void handle(long now) {
        if (now - lastUpdate >= DURATION) {
          lastUpdate = now;
          isRunning = true;
          int i = 0;
          for (Stone stone : stones) {
            stone.move(0, speed[i++]);
          }
        }
      }
    };
    timer.start();
  }

  /**
   * stops the active animation
   */
  public void stop() {
    if (isRunning) {
      timer.stop();
      isRunning = false;
    }
  }

  /**
   * Shuffles an int array.
   * 
   * @param ar The array to be shuffled.
   */
  private static void shuffleArray(int[] ar) {
    Random rnd = ThreadLocalRandom.current();
    for (int i = ar.length - 1; i > 0; i--) {
      int index = rnd.nextInt(i + 1);
      int a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
  }

}
