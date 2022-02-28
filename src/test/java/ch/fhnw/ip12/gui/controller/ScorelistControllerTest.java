package ch.fhnw.ip12.gui.controller;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.fhnw.ip12.constants.ScreenName;
import ch.fhnw.ip12.gui.gameobject.Hand;

public class ScorelistControllerTest {

    @Test
    public void handsMoved() {
        int width = ScreenName.GAME_SCREEN.getScreenWidth();
        int height = ScreenName.GAME_SCREEN.getScreenHeight();

        Hand leftHand = new Hand("/images/left-hand.png", width / 2 - 400, height / 2 - 300);
        Hand rightHand = new Hand("/images/right-hand.png", width / 2 - 200, height / 2 - 300);

        assertNotNull(leftHand);
        assertNotNull(rightHand);
    }
}
