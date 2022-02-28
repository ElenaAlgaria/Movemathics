package ch.fhnw.ip12.gui.gameobject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;


public class GameObjectTest {
  private static final String IMAGES_STONE_PATH = "/images/stone.png";
  private String HAND_IMAGE_PATH = "/images/left-hand.png";

  Hand hand;
  Stone stone;

  @Before
  public void init() {
    hand = new Hand(HAND_IMAGE_PATH, 100, 100);
    stone = new Stone(IMAGES_STONE_PATH, 100, 100, "Test", true);
  }


  @Test
  public void intersectsInXfromLeft() {
    stone.setX(stone.getX() - stone.getWidth());
    assertNotEquals(hand, hand.intersects(stone));

    stone.setX(stone.getX() + 1);
    assertEquals(hand, hand.intersects(stone));
  }

  @Test
  public void intersectsInXfromRight() {
    stone.setX(stone.getX() + hand.getWidth());
    assertNotEquals(hand, hand.intersects(stone));

    stone.setX(stone.getX() - 1);
    assertEquals(hand, hand.intersects(stone));
  }

  @Test
  public void intersectsInYfromTop() {
    stone.setY(stone.getY() - stone.getHeight());
    assertNotEquals(hand, hand.intersects(stone));

    stone.setY(stone.getY() + 1);
    assertEquals(hand, hand.intersects(stone));
  }

  @Test
  public void intersectsInYfromBottom() {
    stone.setY(stone.getY() + hand.getHeight());
    assertNotEquals(hand, hand.intersects(stone));

    stone.setY(stone.getY() - 1);
    assertEquals(hand, hand.intersects(stone));
  }
}
