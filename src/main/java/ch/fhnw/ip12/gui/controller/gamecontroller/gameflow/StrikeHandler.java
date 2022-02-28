package ch.fhnw.ip12.gui.controller.gamecontroller.gameflow;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Updates and handles the strikes, including the displayed images. Has a game
 * over flag to indicate whether the game should be finished.
 */
public class StrikeHandler {

  private static final String RED_CROSS = "/images/redCross.png";
  private static final String GREY_CROSS = "/images/greyCross.png";

  private ImageView[] strikes;
  private Image redCrossImage;
  private Image greyCrossImage;

  private int strikeCount = 0;
  private boolean gameOver;

  /**
   * constructs a new strikehandler.
   * 
   * @param strikes The imageviews to display the strikes
   */
  public StrikeHandler(ImageView[] strikes) {
    this.strikes = strikes;

    this.gameOver = false;
    redCrossImage = new Image(getClass().getResourceAsStream(RED_CROSS));
    greyCrossImage = new Image(getClass().getResourceAsStream(GREY_CROSS));
  }

  /**
   * Increments the strikes and displays a new strike on the imageview. When
   * displaying the last strike the gameover flag is set to true.
   */
  public void addStrike() {
    List<ImageView> strikeImages = Arrays.stream(strikes).filter(s -> !s.getImage().equals(redCrossImage))
        .collect(Collectors.toList());
    if (!strikeImages.isEmpty()) {
      strikeImages.get(0).setImage(redCrossImage);
    }
    strikeCount++;
    if (strikeImages.size() == 1)
      gameOver = true;
  }

  /**
   * Resets the strike counts, the game over flag and all strike images.
   */
  public void reset() {
    strikeCount = 0;
    Arrays.stream(strikes).forEach(strike -> strike.setImage(greyCrossImage));
    gameOver = false;
  }

  /**
   * @return boolean whether the game is over.
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * @return The number of strikes gotten in this game.
   */
  public int getStrikeCount() {
    return strikeCount;
  }

  /**
   * Resets the strike count to 0.
   */
  public void resetStrikeCount() {
    strikeCount = 0;
  }
}
