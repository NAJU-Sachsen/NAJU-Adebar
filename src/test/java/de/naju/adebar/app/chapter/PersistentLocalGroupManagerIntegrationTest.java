package de.naju.adebar.app.chapter;

import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
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

	@BeforeEach
	public void setUp() {
		najuSn = new LocalGroup("NAJU Sachsen", new Address());
		hans = personFactory.buildNew("Hans", "Wurst", Email.of("hw@web.de")).makeActivist().create();
	}

	@Test
	public void testSave() {
		najuSn = localGroupManager.saveLocalGroup(najuSn);
		Assertions.assertTrue(localGroupRepo.existsById(najuSn.getId()), "Group should have been saved");
	}

	@Test
	public void testUpdate() {
		String city = "Leipzig";
		najuSn = localGroupManager.saveLocalGroup(najuSn);

		najuSn.setAddress(new Address("", "", city));
		localGroupManager.updateLocalGroup(najuSn.getId(), najuSn);

		najuSn = localGroupManager.findLocalGroup(najuSn.getId()).orElse(null);
		Assertions.assertEquals(city, najuSn.getAddress().getCity(), "Group should have been updated");
	}

	@Test
	public void testUpdateBoard() {
		hans = personManager.savePerson(hans);
		najuSn = localGroupManager.saveLocalGroup(najuSn);
		Board b = new Board(hans);

		najuSn = localGroupManager.updateBoard(najuSn.getId(), b);
		Assertions.assertEquals(hans, najuSn.getBoard().getChairman(), "Chairman should have been updated");
	}
}
