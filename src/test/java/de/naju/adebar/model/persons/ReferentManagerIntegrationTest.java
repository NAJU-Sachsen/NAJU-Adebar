package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.qualifications.Qualification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic behavior testing of the {@link PersonManager} regarding {@link ReferentProfile}
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
public class ReferentManagerIntegrationTest {

	@Autowired
	private PersonFactory personFactory;
	@Autowired
	private PersonManager personManager;
	@Autowired
	private ReferentProfileRepository referentRepo;
	private PersonId clausId;
	private Person claus;
	private Qualification qualification;

	@BeforeEach
	public void setUp() {
		Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
		this.claus = personFactory.buildNew("Claus", "Störtebecker", Email.of("der_kaeptn@web.de"))
				.makeReferent().create();
		this.claus.setAddress(clausAddress);
		this.qualification = new Qualification("Erste Hilfe Kurs",
				"Hat die Qualifikation, einen Erste-Hilfe Kurs zu leiten");

		this.clausId = claus.getId();
	}

	@Test
	public void testSaveReferent() {
		personManager.savePerson(claus);
		Assertions.assertTrue(referentRepo.existsById(clausId),
				claus.toString() + " should have been saved!");
	}

	@Test
	public void testUpdateReferent() {
		claus = personManager.savePerson(claus);
		claus.getReferentProfile().addQualification(qualification);
		claus = personManager.updatePerson(claus);
		Assertions.assertTrue(claus.getReferentProfile().hasQualification(qualification),
				claus.toString() + " should have been updated!");
	}

	@Test
	public void testIsReferent() {
		personManager.savePerson(claus);
		Person berta = personFactory.buildNew("Berta", "Beate", Email.of("berta@gmx.net")).create();
		personManager.savePerson(berta);

		Assertions.assertFalse(referentRepo.existsById(berta.getId()),
				berta.toString() + " is not an activist");
	}
}
