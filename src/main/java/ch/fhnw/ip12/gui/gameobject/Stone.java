package ch.fhnw.ip12.gui.gameobject;

import ch.fhnw.ip12.MoveMathics;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Class description: A stone displayed on the GUI
 */
public class Stone extends GameObject {
  private static final int WIDTH = 80;
  private static final int HEIGHT = 40;
  private static final int FONT_SIZE = 22;

  private String imagePath;
  private String text;
  private boolean correct;
  private Pane rootPane;
  private boolean selected; // indicates whether stone has been selected
  private Group stoneGroup;
  private ImageView imageView;

  /**
   * constructs a new stone.
   * 
   * @param imagePath the image to display
   * @param x         the default x coordinate
   * @param y         the default y coordinate
   * @param text      the text to display on the stone.
   * @param correct   whether the stone is the correct answer or not
   */
  public Stone(String imagePath, int x, int y, String text, boolean correct) {
    super(x, y, WIDTH, HEIGHT);

    this.imagePath = imagePath;
    this.text = text;
    this.correct = correct;
  }

  @Override
  public void draw(Pane pane) {
    super.draw(pane);
    this.rootPane = pane;
    createStoneImageView();
    layoutStonePane();
    pane.getChildren().add(stoneGroup);
  }

  /**
   * @return boolean whether the stone is the correct answer.
   */
  public boolean isCorrect() {
    return correct;
  }

  /**
   * Updates the image of the stone. it is called when the stone gets selected.
   * 
   * @param imagePath The path to the new image.
   */
  public void displayDifferentStone(String imagePath) {
    this.imagePath = imagePath;
    this.selected = true;
    Image image = new Image(getClass().getResourceAsStream(imagePath), WIDTH, HEIGHT, false, false);
    imageView.setImage(image);
  }

  /**
   * move the stone
   * 
   * @param deltaX
   * @param deltaY
   */
  public void move(int deltaX, int deltaY) {
    if ((this.getY() + this.getHeight()) <= MoveMathics.getStageHeight()) {
      this.setY(this.getY() + deltaY);
      stoneGroup.setLayoutY(this.getY());
    }
  }

  /**
   * remove stone from given pane
   * 
   * @param pane The pane to remove the stone from
   */
  public void removeStone(Pane pane) {
    pane.getChildren().remove(stoneGroup);
  }

  /**
   * @return returns the selected property.
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * can be called to set the selected property of the stone.
   * 
   * @param selected if the stone was selected.
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * @return The text which is dipslayed on the stone
   */
  public String getText() {
    return text;
  }

  private void createStoneImageView() {
    Image image = new Image(getClass().getResourceAsStream(imagePath), WIDTH, HEIGHT, false, false);
    imageView = new ImageView(image);
  }

  private void layoutStonePane() {
    stoneGroup = new Group();
    stoneGroup.setViewOrder(1000.0);
    StackPane stonePane = new StackPane();
    Label label = createLabel();
    stonePane = new StackPane();
    stonePane.setLayoutX(getX());
    stonePane.setLayoutY(getY());
    stonePane.setPrefSize(WIDTH, HEIGHT);
    stonePane.getChildren().addAll(imageView, label);
    StackPane.setAlignment(label, Pos.TOP_CENTER);
    StackPane.setAlignment(imageView, Pos.TOP_LEFT);
    stoneGroup.getChildren().add(stonePane);
  }

  private Label createLabel() {
    Label label = new Label(text);
    label.setFont(new Font(FONT_SIZE));
    label.setMinSize(WIDTH, HEIGHT);
    label.setWrapText(true);
    label.setAlignment(Pos.CENTER);
    label.setTextAlignment(TextAlignment.CENTER);

    return label;
  }
}
