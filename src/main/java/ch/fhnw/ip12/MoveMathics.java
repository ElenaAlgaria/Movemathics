package ch.fhnw.ip12;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Point;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.manager.SoundManager;
import ch.fhnw.ip12.handdetection.PictureRecognition;
import ch.fhnw.ip12.handdetection.PointsContainer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.stage.Stage;

/**
 * Class description: This class contains the main method and starts the opencv
 * process. After that the first screen (startscreen) will be displayed
 */

public class MoveMathics extends Application {
  private static Logger moveMathicsLogger = LogManager.getLogger(MoveMathics.class);
  private static final String APP_TITEL = "MoveMathics";
  private static final String LOGO_PATH = "/images/moveMathicsIcon.jpg";
  private static Stage stage;
  private static ExecutorService executor;
  private static PictureRecognition pictureRecognition;
  private static Media media;

  /**
   * Is used to preload all screens
   */
  @Override
  public void init() throws Exception {
    super.init();
    ScenesManager.getInstance().loadScreens();
  }

  /**
   * starts the fx application, starts the opencv picture recognition
   * 
   * @param stage main stage
   * @throws IOException
   */
  @Override
  public void start(Stage stage) throws IOException {

    MoveMathics.stage = stage;
    ScenesManager.getInstance().switchScene(ScreenName.START_SCREEN);
    ScenesManager.getInstance().getStackPane().setStyle("-fx-font-size: 28px; -fx-background-color: white");

    stage.setScene(new Scene(ScenesManager.getInstance().getStackPane()));
    stage.setTitle(APP_TITEL);
    stage.getIcons().add(new Image(getClass().getResourceAsStream(LOGO_PATH)));
    stage.setFullScreen(true);
    stage.show();

    executor = Executors.newSingleThreadExecutor();
    pictureRecognition = new PictureRecognition();
    executor.execute(pictureRecognition);
    moveMathicsLogger.trace("start picture recognition thread");

    startPositionCapture();
    SoundManager.BACKGROUND_MUSIC.getMusic();
  }

  /**
   * Shuts down the system
   * 
   * @throws IOException
   */
  public static void shutdownSystem() throws IOException {
    pictureRecognition.releaseVideoCapture();
    while (pictureRecognition.isVideoCaptureOpen()) {
    }
    moveMathicsLogger.debug("video capture closed");

    executor.shutdown();
    while (!executor.isShutdown()) {
    }
    moveMathicsLogger.debug("opencv thread shutdown");
    Platform.exit();
    moveMathicsLogger.debug("Platform exit");
    System.exit(0);
  }

  /**
   * The main method to start Movemathics
   * 
   * @param args
   */
  public static void main(String[] args) {
    moveMathicsLogger.trace("start new game");
    launch();
  }

  /**
   * @return the javafx stage
   */
  public static Stage getStage() {
    return stage;
  }

  /**
   * @return the width of the stage
   */
  public static double getStageWidth() {
    return Toolkit.getDefaultToolkit().getScreenSize().width;
  }

  /**
   * @return the height of the stage
   */
  public static double getStageHeight() {
    return Toolkit.getDefaultToolkit().getScreenSize().height;
  }

  private void startPositionCapture() {
    AnimationTimer timer = new AnimationTimer() {
      Point activeLeft;
      Point activeRight;

      @Override
      public void handle(long now) {
        Point left = PointsContainer.getInstance().getLeftPoint();
        Point right = PointsContainer.getInstance().getRightPoint();
        if (left != null && right != null && left != activeLeft && right != activeRight) {
          activeLeft = left;
          activeRight = right;
          HandsManager.getInstance().moveHands(left, right);
        }
      }
    };
    moveMathicsLogger.trace("start position capturing");
    timer.start();
  }
}
