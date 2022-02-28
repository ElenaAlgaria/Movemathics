package ch.fhnw.ip12.gui.manager.visibleevent;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * an event that a scene can react to as soon it is visible again.
 */
public class VisibleEvent extends Event {

  private boolean reloadScene = false;

  public static final EventType<VisibleEvent> CUSTOM_EVENT_TYPE = new EventType<>(ANY, "visible");

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * constructs a new visible event.
   */
  public VisibleEvent() {
    super(CUSTOM_EVENT_TYPE);
  }

  /**
   * constructs a new visible event.
   * 
   * @param reloadScene indicates with a flag if the scene should be reloaded.
   *                    this flag can be accessed in the event handler
   */
  public VisibleEvent(boolean reloadScene) {
    super(CUSTOM_EVENT_TYPE);
    this.reloadScene = reloadScene;
  }

  /**
   * @return whether the scene should be reloaded.
   */
  public boolean shouldReloadScene() {
    return reloadScene;
  }

  public void setReloadScene(boolean reloadScene) {
    this.reloadScene = reloadScene;
  }

}
