package ch.fhnw.ip12.gui.controller.gamecontroller.gameflow;

import java.util.Arrays;

import ch.fhnw.ip12.gui.model.Player;
import javafx.scene.image.ImageView;

/**
 * Represents and builds the tower in the lower right corner of the movematics
 * game.
 */
public class Tower {

  private ImageView[][] stones;
  private ImageView[] decoration;

  /**
   * Constructs an new tower
   * 
   * @param tower           The images of the tower is a two dimensional array:
   *                        the first dimension has a counter two access the tower
   *                        using the achieved points, the second dimension has
   *                        the images of the tower.
   * @param towerDecoration The images of the tower decoration.
   */
  public Tower(ImageView[][] tower, ImageView[] towerDecoration) {
    this.stones = tower;
    this.decoration = towerDecoration;
  }

  /**
   * Increases the tower, with every round two stones are added. When gaining the
   * last point, the decoration is added as well.
   */
  public void buildTower() {
    stones[Player.getInstance().getPoints()][0].setOpacity(1);
    stones[Player.getInstance().getPoints()][1].setOpacity(1);

    if (Player.getInstance().getPoints() == 11) {
      Arrays.stream(decoration).forEach(image -> image.setOpacity(1));
    }
  }

  /**
   * Sets the opacity of each imagview to 0, to undisplay the tower.
   */
  public void reset() {
    Arrays.stream(stones).flatMap(Arrays::stream).forEach(e -> e.setOpacity(0));
    Arrays.stream(decoration).forEach(image -> image.setOpacity(0));
  }
}
