
package ch.fhnw.ip12.gui.manager;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import ch.fhnw.ip12.gui.gameobject.Hand;
import ch.fhnw.ip12.gui.gameobject.Stone;

public class HandsManagerTest {
   Hand hand;
   Stone stone;
   HandsManager handsManager;

   @Before
   public void getObject() {
      handsManager = HandsManager.getInstance();
   }

   @Test
   public void getInstance() {
      assertNotNull(handsManager);
   }

}
