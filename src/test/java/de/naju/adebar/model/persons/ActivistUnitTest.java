package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.details.JuleicaCard;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Basic testing of the {@link ActivistProfile} class
 *
 * @author Rico Bergmann
 */
public class ActivistUnitTest {

	private Person hans;

	@BeforeEach
	public void setUp() {
		hans = new Person(new PersonId(), "Hans", "Wurst", Email.of("hans@web.de"));
		hans.makeActivist();
	}

	@Test
	public void testHasJuleica() {
		hans.getActivistProfile().setJuleicaCard(new JuleicaCard(LocalDate.now()));
		Assertions.assertTrue(hans.getActivistProfile().hasJuleica(),
				"Activist should have a JuLeiCa card");
	}

	@Test
	public void testHasNoJuleica() {
		Assertions.assertFalse(hans.getActivistProfile().hasJuleica(),
				"Activist should have no JuLeiCa card");
	}

	@Test
	public void testRemoveJuleica() {
		hans.getActivistProfile().setJuleicaCard(new JuleicaCard(LocalDate.now()));
		hans.getActivistProfile().setJuleicaCard(null);
		Assertions.assertFalse(hans.getActivistProfile().hasJuleica(),
				"Activist should have no JuLeiCa card");
	}

}
