package ch.fhnw.ip12.gui.controller.gamecontroller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.controller.gamecontroller.gameflow.GameFlow;
import ch.fhnw.ip12.gui.controller.gamecontroller.gameflow.StrikeHandler;
import ch.fhnw.ip12.gui.controller.gamecontroller.gameflow.Tower;
import ch.fhnw.ip12.gui.gameobject.FxGameButton;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.animation.ColorConstants;
import ch.fhnw.ip12.gui.input.HandControllable;
import ch.fhnw.ip12.gui.jfxcomponents.CountdownLabel;
import ch.fhnw.ip12.gui.manager.HandsManager;
import ch.fhnw.ip12.gui.manager.ScenesManager;
import ch.fhnw.ip12.gui.manager.visibleevent.InvisibleEvent;
import ch.fhnw.ip12.gui.manager.visibleevent.VisibleEvent;
import ch.fhnw.ip12.gui.reader.ExerciseReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Class description: Controller class of game scene
 */
public class GameController implements HandControllable {
  private static Logger gameControllerLogger = LogManager.getLogger(GameController.class);

  private static final ScreenName ID = ScreenName.GAME_SCREEN;

  private ExerciseReader exerciseReader;
  private FxGameButton menuButton;
  private CountdownLabel countdownLabel;
  private Tower tower;
  private StrikeHandler strikeHandler;
  private GameFlow gameFlow;
  @FXML
  private Button menuButtonFX;

  @FXML
  private Pane pane;

  @FXML
  private ImageView stone1;

  @FXML
  private ImageView stone2;

  @FXML
  private ImageView stone3;

  @FXML
  private ImageView stone4;

  @FXML
  private ImageView stone5;

  @FXML
  private ImageView stone6;

  @FXML
  private ImageView stone7;

  @FXML
  private ImageView stone8;

  @FXML
  private ImageView stone9;

  @FXML
  private ImageView stone10;

  @FXML
  private ImageView stone11;

  @FXML
  private ImageView stone12;

  @FXML
  private ImageView stone13;

  @FXML
  private ImageView stone14;

  @FXML
  private ImageView stone15;

  @FXML
  private ImageView stone16;

  @FXML
  private ImageView stone17;

  @FXML
  private ImageView stone18;

  @FXML
  private ImageView stone19;

  @FXML
  private ImageView stone20;

  @FXML
  private ImageView stone21;

  @FXML
  private ImageView stone22;

  @FXML
  private ImageView stone23;

  @FXML
  private ImageView stone24;

  @FXML
  private ImageView stone25;

  @FXML
  private ImageView bannerLeft;

  @FXML
  private ImageView bannerRight;

  @FXML
  private ImageView flagLeft;

  @FXML
  private ImageView flagRight;

  @FXML
  private ImageView strike1;

  @FXML
  private ImageView strike2;

  @FXML
  private ImageView strike3;

  @FXML
  private VBox vbox;

  @FXML
  private Label rechnungLbl;
  @FXML
  private Label scoreLbl;

  /**
   * Is called when the hands have been moved and handles intersections with the
   * stones and the buttons.
   * 
   * @param leftHand  the left hand
   * @param rightHand the right hand
   */
  @Override
  public void handsMoved(Hand leftHand, Hand rightHand) {
    if (menuButton == null || menuButton.getX() == 0) {
      menuButton = new FxGameButton(menuButtonFX, ColorConstants.BLUE);
    }
    handleIntersection(leftHand, rightHand);
    gameFlow.handleHandPositionChange(leftHand, rightHand);
  }

  /**
   * @return ScreenName the Enum represenation screenname of the gamecontroller.
   */
  @Override
  public ScreenName getID() {
    return ID;
  }

  /**
   * Initializes the gameController, this includes: all controls, the tower the
   * strikes and the gameflow.
   */
  @FXML
  protected void initialize() {
    gameControllerLogger.traceEntry("initialize");
    HandsManager.getInstance().register(this);
    exerciseReader = ExerciseReader.getInstance();

    initializeControls();
    initializeTower();
    initializeStrike();
    reactToSetVisibleEvents();
    gameFlow = new GameFlow(pane, tower, strikeHandler, scoreLbl, rechnungLbl, countdownLabel);
  }

  private void initializeControls() {
    rechnungLbl.setText("");
    rechnungLbl.setTranslateX(ScreenName.GAME_SCREEN.getScreenWidth() / 10 - 218);
    countdownLabel = new CountdownLabel();
    pane.getChildren().add(countdownLabel);
  }

  private void initializeStrike() {
    ImageView[] strikes = { strike1, strike2, strike3 };
    strikeHandler = new StrikeHandler(strikes);
  }

  private void initializeTower() {
    ImageView[][] stones = { { stone1, stone2, }, { stone3, stone4, }, { stone5, stone6, }, { stone7, stone8, },
        { stone9, stone10, }, { stone11, stone12, }, { stone13, stone14, }, { stone15, stone16, },
        { stone17, stone18, }, { stone19, stone20, }, { stone21, stone22, }, { stone23, stone25, } };
    ImageView[] towerDecoration = { bannerLeft, bannerRight, flagLeft, flagRight, stone24 };
    tower = new Tower(stones, towerDecoration);
  }

  private void reactToSetVisibleEvents() {
    pane.addEventHandler(VisibleEvent.CUSTOM_EVENT_TYPE, event -> {
      if (event.shouldReloadScene()) {
        gameFlow.restart();
      } else {
        gameFlow.continueGame();
      }
    });

    pane.addEventHandler(InvisibleEvent.CUSTOM_EVENT_TYPE,
        event -> this.exerciseReader.activeExercise().stopAnimation());
  }

  /**
   * @param event
   */
  @FXML
  private void menuBtn(ActionEvent event) {
    ScenesManager.getInstance().switchScene(ScreenName.MENU_SCREEN);
  }

  /**
   * @param leftHand
   * @param rightHand
   */
  private void handleIntersection(Hand leftHand, Hand rightHand) {
    if (leftHand.intersects(menuButton) != null || rightHand.intersects(menuButton) != null) {
      menuButton.fireEventWithAnimation();
    }
  }
}
