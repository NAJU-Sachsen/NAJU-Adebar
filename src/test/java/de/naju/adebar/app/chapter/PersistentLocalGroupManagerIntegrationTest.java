package de.naju.adebar.app.chapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonFactory;
import de.naju.adebar.model.human.PersonManager;

/**
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Component
public class PersistentLocalGroupManagerIntegrationTest {
  @Autowired
  private PersistentLocalGroupManager localGroupManager;
  @Autowired
  private LocalGroupRepository localGroupRepo;
  @Autowired
  private PersonFactory personFactory;
  @Autowired
  private PersonManager personManager;

  private LocalGroup najuSn;
  private Person hans;

  @Before
  public void setUp() {
    najuSn = new LocalGroup("NAJU Sachsen", new Address());
    hans = personFactory.buildNew("Hans", "Wurst", "hw@web.de").makeActivist().create();
  }

  @Test
  public void testSave() {
    najuSn = localGroupManager.saveLocalGroup(najuSn);
    Assert.assertTrue("Group should have been saved", localGroupRepo.exists(najuSn.getId()));
  }

  @Test
  public void testUpdate() {
    String city = "Leipzig";
    najuSn = localGroupManager.saveLocalGroup(najuSn);

    najuSn.setAddress(new Address("", "", city));
    localGroupManager.updateLocalGroup(najuSn.getId(), najuSn);

    najuSn = localGroupManager.findLocalGroup(najuSn.getId()).orElse(null);
    Assert.assertEquals("Group should have been updated", city, najuSn.getAddress().getCity());
  }

  @Test
  public void testUpdateBoard() {
    hans = personManager.savePerson(hans);
    najuSn = localGroupManager.saveLocalGroup(najuSn);
    Board b = new Board(hans);

    najuSn = localGroupManager.updateBoard(najuSn.getId(), b);
    Assert.assertEquals("Chairman should have been updated", hans, najuSn.getBoard().getChairman());
  }
}
