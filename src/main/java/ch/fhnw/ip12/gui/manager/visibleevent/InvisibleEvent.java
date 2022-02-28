package ch.fhnw.ip12.gui.manager.visibleevent;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * An event that a scene can react to as soon it is invisible again.
 */
public class InvisibleEvent extends Event {

  public static final EventType<InvisibleEvent> CUSTOM_EVENT_TYPE = new EventType<>(ANY, "invisible");

  private static final long serialVersionUID = 1L;

  /**
   * constructs a new invisibleevent
   */
  public InvisibleEvent() {
    super(CUSTOM_EVENT_TYPE);
  }

}
