package ch.fhnw.ip12.gui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Delivers data (playername and score) to the tableview - scoreboard
 * Create connection between variables and table view over Propertys
 */
public class Score {

    private final SimpleStringProperty name;
    private final SimpleIntegerProperty score;

    /**
     *
     * Constructor
     *
     * @param name of the player
     * @param score of the player
     */
    public Score(String name, int score) {
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
    }

    /**
     *
     * @return name
     */
    public String getName(){ return name.get(); }

    /**
     *
     * @return score
     */
    public int getScore(){ return score.get(); }

    /**
     *
     * @param name to set
     */
    public void setName(String name){ this.name.set(name); }

    /**
     *
     * @param score
     */
    public void setScore(int score){ this.score.set(score); }
}
