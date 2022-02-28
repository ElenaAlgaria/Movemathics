package ch.fhnw.ip12.handdetection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Point;

public class PointsContainerTest {
  PointsContainer container;

  @Before
  public void getObject() {
    container = PointsContainer.getInstance();
  }

  @Test
  public void getInstance() {
    assertNotNull(container);
  }

  @Test
  public void addHandPositions() {
    container.addHandPositions(new Point(0, 0), new Point(0, 0));

    assertEquals(new Point(0, 0), container.getLeftPoint());
    assertEquals(new Point(0, 0), container.getRightPoint());

    container.addHandPositions(new Point(1, 0), new Point(0, 1));

    assertEquals(new Point(1, 0), container.getLeftPoint());
    assertEquals(new Point(0, 1), container.getRightPoint());

    container.addHandPositions(new Point(11, 21), new Point(31, 41));
    container.addHandPositions(new Point(12, 22), new Point(32, 42));
    container.addHandPositions(new Point(13, 23), new Point(33, 43));

    assertEquals(new Point(13, 23), container.getLeftPoint());
    assertEquals(new Point(33, 43), container.getRightPoint());
  }

  @Test
  public void addNull() {
    container.addHandPositions(null, null);

    assertNull(container.getLeftPoint());
    assertNull(container.getRightPoint());

    container.addHandPositions(new Point(0, 0), new Point(0, 0));
    assertEquals(new Point(0, 0), container.getLeftPoint());
    assertEquals(new Point(0, 0), container.getRightPoint());

  }
}