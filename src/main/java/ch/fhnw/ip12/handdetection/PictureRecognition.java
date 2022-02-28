package ch.fhnw.ip12.handdetection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

import ch.fhnw.ip12.constants.ScreenName;

/**
 * Class description: This class uses opencv to recognize green color and
 * evaluate the center points of the two largest green parts on the frame
 */

public final class PictureRecognition implements Runnable {
  private static Logger recognitionLogger = LogManager.getLogger(PictureRecognition.class);

  private static final boolean DEBUG_MODE = false;
  private static final int SCALED_FRAME_WIDTH = 300;
  private static final int SCALED_FRAME_HEIGHT = 225;
  private VideoCapture vc;
  private Mat debugFrame;
  private AtomicBoolean isRunning;

  /**
   * Runs the picture recognition.
   */
  @Override
  public void run() {
    try {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    } catch (Exception e) {
      recognitionLogger.error("load opencv library error: %s", e.getMessage());
    }

    // Create Video Stream on Video Input 0
    vc = new VideoCapture(0);

    // Create bg (backgound substraction) with MOG2 algorithm
    BackgroundSubtractorMOG2 backSub = Video.createBackgroundSubtractorMOG2(20, 50, false);

    if (!vc.isOpened()) {
      recognitionLogger.warn("can not open video input");
      System.exit(0);
    }

    // Create Mat for video input and green color mask
    Mat frame = new Mat();
    Mat frameHSV = new Mat();
    Mat originframe = new Mat();

    if (DEBUG_MODE)
      debugFrame = new Mat();

    Scalar lowGreen = new Scalar(47, 70, 52);
    Scalar upGreen = new Scalar(82, 212, 193);

    // Size for scaling the frame
    Size scaleSize = new Size(SCALED_FRAME_WIDTH, SCALED_FRAME_HEIGHT);

    isRunning = new AtomicBoolean();
    isRunning.set(true);
    recognitionLogger.trace("start picture recognition");

    while (vc.isOpened()) {
      // capture a frame from webcam stream
      vc.read(originframe);

      // adjust the frame size for a better performance
      Imgproc.resize(originframe, originframe, scaleSize);

      // mirroring the frame
      Core.flip(originframe, originframe, 1);

      applyBackgroundSubtraction(backSub, originframe, frame);

      if (DEBUG_MODE)
        debugFrame = frame.clone();

      // blur the image for less noise
      Imgproc.medianBlur(frame, frame, 7);

      // convert from bgr to hsv
      Imgproc.cvtColor(frame, frameHSV, Imgproc.COLOR_BGR2HSV);

      Mat mask = createBWMask(frameHSV, lowGreen, upGreen);

      Point[] handPoints = findHandPoints(frame, mask.clone());

      // check if the points of hands are real numbers and send to game
      sendPoints(handPoints);

      if (DEBUG_MODE) {
        displayFrames(frame, originframe, mask);
      }
      if (!isRunning.get()) {
        recognitionLogger.debug("closing video capture");
        HighGui.destroyAllWindows();
        vc.release();
      }
    }
  }

  /**
   * @param backSub
   * @param dst
   * @param src
   */
  private void applyBackgroundSubtraction(BackgroundSubtractorMOG2 backSub, Mat src, Mat dst) {
    // create forground-mask with bg-substraction
    backSub.apply(src, dst, 0.0005);
    Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);
    Core.bitwise_and(src, dst, dst);
  }

  public void releaseVideoCapture() {
    isRunning.set(false);
  }

  /**
   * @return boolean
   */
  public boolean isVideoCaptureOpen() {
    return vc.isOpened();
  }

  /**
   * @param frame
   * @param mask
   * @return Point[]
   */
  private Point[] findHandPoints(Mat frame, Mat mask) {
    Point[] handPoints = { new Point(0, 0), new Point(0, 0) };
    for (int i = 0; i < handPoints.length; i++) {

      // search for contours in b/w - mask
      List<MatOfPoint> contourList = searchContours(mask);

      if (DEBUG_MODE) {
        for (int j = 0; j < contourList.size(); j++) {
          Imgproc.drawContours(debugFrame, contourList, j, new Scalar(0, 0, 255), 2);
        }
      }

      if (!contourList.isEmpty()) {

        // get contour with the biggest area
        int contourIndex = getIndexOfBiggestContour(contourList);

        // get center point of the biggest contour
        handPoints[i] = getCenterPoint(contourList.get(contourIndex));

        // paint over the biggest contour with black color in mask mat
        paintOverContour(contourList, contourIndex, mask);

        // mark the hand points on the original frame with a blue cross
        if (DEBUG_MODE)
          drawIntoFrame(debugFrame, handPoints[i], new Scalar(255, 0, 0));

        // mapping the points of gui screen size
        handPoints[i] = mapPoint2Gui(handPoints[i], frame.rows(), frame.cols());
      }
    }
    return handPoints;
  }

  /**
   * @param handPoints
   */
  private void sendPoints(Point[] handPoints) {
    if (!(Double.isNaN(handPoints[0].x) || Double.isNaN(handPoints[1].x))) {
      // sending hand position in format (left , right)
      if ((handPoints[0].x > handPoints[1].x)) {
        sendPositionsToGame(handPoints[1], handPoints[0]);
      } else {
        sendPositionsToGame(handPoints[0], handPoints[1]);
      }
    }
  }

  /**
   *
   * @param mask mat with detected color range
   * @return location of a hand
   */

  private List<MatOfPoint> searchContours(Mat mask) {
    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();

    // search contours of form in the frame
    Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
    return contours;
  }

  /**
   * 
   * @param contours
   * @param contourIndex
   * @param mask
   * @return
   */
  private Mat paintOverContour(List<MatOfPoint> contours, int contourIndex, Mat mask) {

    Imgproc.drawContours(mask, contours, contourIndex, new Scalar(0, 0, 0), -1);
    return mask;
  }

  /**
   * 
   * @param contours detected contours in b/w mask
   * @return the index of the biggest contour
   */
  private int getIndexOfBiggestContour(List<MatOfPoint> contours) {
    // variables for detection of biggest contour
    double area;
    double biggestArea = 0.0d;
    int numberOfBiggestArea = 0;

    // check all detected contours and save the index of the biggest
    for (int i = 0; i < contours.size(); i++) {
      area = Imgproc.contourArea(contours.get(i));
      if (area < 100)
        continue;
      if (biggestArea < area) {
        biggestArea = area;
        numberOfBiggestArea = i;
      }
    }
    return numberOfBiggestArea;
  }

  /**
   * 
   * @param mop mat of points
   * @return center Point
   */
  private Point getCenterPoint(MatOfPoint mop) {

    double x = 0;
    double y = 0;

    if (mop != null) {
      // calculate center of the contour
      Moments m = Imgproc.moments(mop);
      x = m.m10 / m.m00;
      y = m.m01 / m.m00;
    }
    return new Point(x, y);
  }

  /**
   *
   * @param frame capture from the video cam
   * @param point center point of a hand
   * @param color to draw the markers and text
   */
  private void drawIntoFrame(Mat frame, Point point, Scalar color) {
    int x = (int) point.x;
    int y = (int) point.y;
    Imgproc.drawMarker(frame, point, color, Imgproc.MARKER_CROSS, 25);
    Imgproc.putText(frame, "" + x + " / " + y, new Point(x - 50.0d, y - 25.0d), 1, 1, color, 2);
  }

  /**
   * search for a given color range.
   * 
   * @param frameHSV
   * @param lowGreen threshhold lower green space
   * @param upGreen  threshhold upper green space
   * @return bw mask
   */
  private Mat createBWMask(Mat frameHSV, Scalar lowGreen, Scalar upGreen) {
    Mat mask = new Mat();
    // create a mask of a green color range
    Core.inRange(frameHSV, lowGreen, upGreen, mask);
    return mask;
  }

  /**
   * 
   * @param centerPoint of a hand
   * @param rows        frame height
   * @param cols        frame width
   * @return
   */
  private Point mapPoint2Gui(Point centerPoint, int rows, int cols) {

    double xMap = ((double) ScreenName.GAME_SCREEN.getScreenWidth() / (double) cols) * centerPoint.x;
    double yMap = ((double) ScreenName.GAME_SCREEN.getScreenHeight() / (double) rows) * centerPoint.y;
    return new Point(xMap, yMap);
  }

  /**
   * 
   * @param centerPoint1 left hand
   * @param centerPoint2 right hand
   */
  private void sendPositionsToGame(Point centerPoint1, Point centerPoint2) {
    if ((centerPoint1.x != 0.0 && centerPoint1.y != 0.0) && (centerPoint2.x != 0 && centerPoint2.y != 0)) {
      PointsContainer.getInstance().addHandPositions(centerPoint1, centerPoint2);

    }
  }

  /**
   * Display several frames for debugging
   */
  private void displayFrames(Mat frame, Mat originframe, Mat mask) {
    // display frame
    HighGui.imshow("original frame", originframe);
    HighGui.moveWindow("original frame", 0, 0);
    HighGui.imshow("debug Frame", debugFrame);
    HighGui.moveWindow("debug Frame", originframe.rows() + frame.rows(), 0);
    HighGui.imshow("frame", frame);
    HighGui.moveWindow("frame", originframe.rows() + frame.rows(), frame.cols());
    HighGui.imshow("green mask", mask);
    HighGui.moveWindow("green mask", originframe.rows() + frame.rows(), 2 * frame.cols());

    // wait for next frame
    HighGui.waitKey(20);
    // press Escape to leave the programm (press a few seconds !)
    if (HighGui.pressedKey == 27) {
      HighGui.destroyAllWindows();
      vc.release();
      recognitionLogger.debug("exit picture recognition");
      System.exit(0);
    }
  }

}
