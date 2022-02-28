package ch.fhnw.ip12.gui.manager;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.manager.visibleevent.InvisibleEvent;
import ch.fhnw.ip12.gui.manager.visibleevent.VisibleEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * This class handles the scene switch.
 */
public class ScenesManager {

  private static final String HAND_SCREEN_FXML = "/views/Hand.fxml";
  private static final String FONT_PATH = "/fonts/Itim-Regular.ttf";
  private StackPane pane;
  private Map<ScreenName, Parent> screens;

  private static final ScenesManager INSTANCE = new ScenesManager();

  /**
   * Singleton
   * 
   * @return scenesManager
   */
  public static ScenesManager getInstance() {
    return INSTANCE;
  }

  private ScenesManager() {
  }

  /**
   * sets the active scene invsible, and the given visible
   * 
   * @param newScreen the Screen to setVisible
   */
  public void switchScene(ScreenName newScreen) {
    this.switchScene(newScreen, false);
  }

  /**
   * Loads all screens
   * 
   * @throws IOException
   */
  public void loadScreens() throws IOException {
    Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 10);
    screens = new EnumMap<>(ScreenName.class);
    pane = new StackPane();
    for (ScreenName screenName : ScreenName.values()) {
      final Parent screen = FXMLLoader.load(getClass().getResource(screenName.getPath()));
      screens.put(screenName, screen);
      screens.get(screenName).setVisible(false);
      screens.get(screenName).setDisable(true);
      pane.getChildren().add(screens.get(screenName));
    }
    addHandScreen();
  }

  /**
   * @return returns the acitve pane
   */
  public StackPane getStackPane() {
    return pane;
  }

  /**
   * @return returns all screens
   */
  public Map<ScreenName, Parent> getScreens() {
    return screens;
  }

  /**
   * sets the active scene invsible, and the given visible
   * 
   * @param newScreen the Screen to setVisible
   * @param b         whether it should call the visibleEvent with restart
   *                  property or not
   */
  public void switchScene(ScreenName newScreen, boolean b) {
    screens.entrySet().stream().map(Entry::getValue).filter(Node::isVisible).forEach(s -> {
      s.setVisible(false);
      s.setDisable(true);
      s.fireEvent(new InvisibleEvent());
    });
    screens.get(newScreen).setDisable(false);
    screens.get(newScreen).setVisible(true);
    screens.get(newScreen).fireEvent(new VisibleEvent(b));
    HandsManager.getInstance().changeActiveMovementConsumer(newScreen);
  }

  /**
   * Adds the screen with the hands displayed on.
   * 
   * @throws IOException
   */
  private void addHandScreen() throws IOException {
    Parent handScreen = FXMLLoader.load(getClass().getResource(HAND_SCREEN_FXML));
    handScreen.setVisible(true);
    handScreen.setDisable(true);
    pane.getChildren().add(handScreen);
  }

}
