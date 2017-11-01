package de.naju.adebar.app.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class PersistentEventManagerIntegrationTest {
  @Autowired
  private PersistentEventManager eventManager;
  @Autowired
  private EventFactory eventFactory;

  private Event hifa;

  @Before
  public void setUp() {
    hifa = eventFactory.build("HIFA", LocalDateTime.now().minusDays(1),
        LocalDateTime.now().plusDays(1));
  }

  @Test
  public void testSave() {
    eventManager.saveEvent(hifa);
  }

  @Test
  public void testUpdate() {
    String newName = "afih";
    hifa = eventManager.saveEvent(hifa);
    hifa.setName(newName);
    hifa = eventManager.updateEvent(hifa.getId().toString(), hifa);
    Assert.assertEquals("Not updated correctly", newName, hifa.getName());
  }

}
