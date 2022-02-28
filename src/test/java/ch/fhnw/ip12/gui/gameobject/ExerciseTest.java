package ch.fhnw.ip12.gui.gameobject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExerciseTest {
    Hand hand;
    Exercise exercise;

    @Test
    public void intersects() {
        hand = new Hand("/images/left-hand.png", 150, 200);
        Stone[] stone = {new Stone("/images/stone.png", 118, 248, "2", true),
                new Stone("/images/stone.png", 148, 248, "2", false)};
        exercise = new Exercise(200, 30, 1, "1 + 2 = ?", stone);

        assertEquals(stone[0], exercise.intersects(hand));
        assertNotNull(exercise.intersects(hand));

        Stone[] stone2 = {new Stone("/images/stone.png", 18, 28, "2", true),
                new Stone("/images/stone.png", 18, 28, "2", false)};
        exercise = new Exercise(200, 30, 1, "1 + 2 = ?", stone2);

        assertNull(exercise.intersects(hand));
    }
}
