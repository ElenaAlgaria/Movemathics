package ch.fhnw.ip12.gui.gameobject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StoneTest {

    Stone stone;
    Stone stone2;


    @Test
    public void isCorrect() {
        stone = new Stone(null, 50, 50, "2", true);
        stone2 = new Stone(null, 50, 50, "2", false);

        assertEquals(true, stone.isCorrect());
        assertEquals(false, stone2.isCorrect());
    }
}
