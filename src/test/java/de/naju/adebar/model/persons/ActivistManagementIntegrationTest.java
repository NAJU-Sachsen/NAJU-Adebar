package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.details.JuleicaCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic behavior testing for the {@link PersistentPersonManager} and its behavior on
 * {@link ActivistProfile} instances
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
public class ActivistManagementIntegrationTest {

	@Autowired
	private PersonFactory personFactory;
	@Autowired
	private PersonManager personManager;

	@Autowired
	private ActivistProfileRepository activistRepo;
	private Person claus;

	@BeforeEach
	public void setUp() {
		Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
		this.claus = personFactory.buildNew("Claus", "Störtebecker", Email.of("der_kaeptn@web.de"))
				.makeActivist().create();
		claus.setAddress(clausAddress);

		personManager.savePerson(claus);
	}

	@Test
	public void testSaveActivist() {
		Assertions.assertTrue(activistRepo.existsById(claus.getId()),
				claus.toString() + " should have been saved!");
	}

	@Test
	public void testUpdateActivist() {

		// update the person
		claus = personManager.repository().findEntry(claus.getId());
		claus.getActivistProfile().setJuleicaCard(new JuleicaCard());

		// save the updated person
		claus = personManager.updatePerson(claus);

		Assertions.assertTrue(claus.getActivistProfile().hasJuleica(), "Person should have Juleica card");
	}

}
