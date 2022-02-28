package ch.fhnw.ip12.gui.manager;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class ScenesManagerTest {
    ScenesManager scenesManager;

    @Before
    public void getObject() {
        scenesManager = ScenesManager.getInstance();
    }

    @Test
    public void getInstance() {
        assertNotNull(scenesManager);
    }
}
