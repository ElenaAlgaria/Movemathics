package ch.fhnw.ip12.handdetection;

import java.util.concurrent.atomic.AtomicReference;

import org.opencv.core.Point;

/**
 * Class description: This calss stores hand position data from picture
 * recognition class in a threadsafe datastructure for a clean transfer to the
 * javaFX thread.
 */
public final class PointsContainer {

  private static final PointsContainer INSTANCE = new PointsContainer();
  private AtomicReference<Point> leftHand = new AtomicReference<>();
  private AtomicReference<Point> rightHand = new AtomicReference<>();

  private PointsContainer() {
  }

  /**
   * @return PointsContainer
   */
  public static PointsContainer getInstance() {
    return INSTANCE;
  }

  /**
   * @param leftHandPoint
   * @param rightHandPoint
   */
  public void addHandPositions(Point leftHandPoint, Point rightHandPoint) {
    if (leftHandPoint != null && rightHandPoint != null) {
      leftHand.set(leftHandPoint);
      rightHand.set(rightHandPoint);
    }
  }

  /**
   * @return Point
   */
  public Point getLeftPoint() {
    return leftHand.get();
  }

  /**
   * @return Point
   */
  public Point getRightPoint() {
    return rightHand.get();
  }
}
