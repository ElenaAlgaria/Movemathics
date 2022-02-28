package ch.fhnw.ip12.gui.model;

/**
 * Class description: Class for Player with all his data
 */

public class Player {

    private static final Player INSTANCE = new Player();
    private String name;
    private int points;

    private Player(){}

    /**
     * @return instance of Player
     */
    public static Player getInstance() {
        return INSTANCE;
    }

    /**
     *
     * @param name of Player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param points reached
     */
    public void setPoints(int points) { this.points = points; }

    /**
     *
     * @return Player Name
     */
    public String getName() { return name; }

    /**
     *
     * @return reached Points
     */
    public int getPoints() {
        return points;
    }

    public void incPoints(){
      this.points ++;
    }
}
