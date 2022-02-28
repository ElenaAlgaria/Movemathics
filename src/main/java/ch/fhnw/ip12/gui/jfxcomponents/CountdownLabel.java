package ch.fhnw.ip12.gui.jfxcomponents;

import ch.fhnw.ip12.MoveMathics;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * A CountdownLabel can be added to a pane to display a square with a text, a
 * countdown from 3 to 1 and a callback funciton after the countdown.
 */
public class CountdownLabel extends StackPane {
  private static final int COUNTDOWN_LABEL_WIDTH = 400;
  private static final int COUNTDOWN_LABEL_HEIGHT = 300;
  private static final int VBOX_SPACING = 20;
  private Runnable nextExercise;
  private Label countdown = new Label();
  private Label textHeading = new Label();
  private ImageView imageView = new ImageView();

  /**
   * constructs a new countdown label
   */
  public CountdownLabel() {
    super();
    setVisible(false);
    VBox vbox = new VBox(VBOX_SPACING);
    vbox.getChildren().addAll(textHeading, countdown);
    getChildren().addAll(imageView, vbox);
    layoutCountDownNumber();
    layoutHeading();
  }

  /**
   * Starts the coutndown
   * 
   * @param countdownType        The type of the countdown.
   * @param nextExerciseRunnable the callback as runnable to execute as soon as
   *                             the countdown has finished.
   */
  public void start(CountdownType countdownType, Runnable nextExerciseRunnable) {
    nextExercise = nextExerciseRunnable;
    imageView = createBackgroundImage(countdownType);
    textHeading.setTextFill(countdownType.getColor());
    textHeading.setText(countdownType.getMessage());
    countdown.setTextFill(countdownType.getColor());

    setMaxSize(COUNTDOWN_LABEL_WIDTH, COUNTDOWN_LABEL_HEIGHT);
    setLayoutX(MoveMathics.getStageWidth() / 2);
    setLayoutY(MoveMathics.getStageHeight() / 2);
    createAnimation().start();
  }

  /**
   * Sets alignment to the coutndown label
   */
  private void layoutCountDownNumber() {
    countdown.setStyle("-fx-font: 70 system;");
    countdown.setAlignment(Pos.CENTER);
    countdown.setMinWidth(COUNTDOWN_LABEL_WIDTH);
    countdown.setWrapText(true);
  }

  private void layoutHeading() {
    textHeading.setStyle("-fx-font: 30 system;");
    textHeading.setMinWidth(COUNTDOWN_LABEL_WIDTH);
    textHeading.setMinHeight(100);
    textHeading.setTextAlignment(TextAlignment.CENTER);
    textHeading.setAlignment(Pos.CENTER);
    textHeading.setWrapText(true);
  }

  private ImageView createBackgroundImage(CountdownType countdownType) {
    Image image = new Image(getClass().getResourceAsStream(countdownType.getImage()), COUNTDOWN_LABEL_WIDTH,
        COUNTDOWN_LABEL_HEIGHT, false, false);
    imageView.setImage(image);
    imageView.setViewOrder(1000);
    imageView.setOpacity(0.7);
    return imageView;
  }

  /**
   * returns an aniimationtimer. with every tick it is checked if a second has
   * passed since the last tick, if yes the countdown is decreased by one. If the
   * countdown would go from 1 to 0 the runnable defined in the startmehod will be
   * executed.
   * 
   * @return the animationtimer
   */
  private AnimationTimer createAnimation() {
    return new AnimationTimer() {
      private long startTime;
      private int count = 1;
      private int count2 = 2;
      private int count3 = 3;

      @Override
      public void handle(long now) {
        if (startTime == 0) {
          setVisible(true);
          countdown.setText(String.valueOf(count3));
          startTime = now;
        }

        long animationDuration = now - startTime;

        if (animationDuration > count * 1_000_000_000f) {
          countdown.setText(String.valueOf(count2--));
          if (count == 3) {
            stop();
            nextExercise.run();
            setVisible(false);
            countdown.setText("");
            textHeading.setText("");
          }
          count++;
        }
      }
    };
  }

}
