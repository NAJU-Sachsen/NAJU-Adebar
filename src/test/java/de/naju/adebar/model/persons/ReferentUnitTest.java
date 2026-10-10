package de.naju.adebar.model.persons;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.Iterables;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.qualifications.Qualification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Basic testing of the {@link ReferentProfile} class
 *
 * @author Rico Bergmann
 */
public class ReferentUnitTest {

	private Person referent;
	private Qualification qualification;

	@BeforeEach
	public void setUp() {
		referent = new Person(new PersonId(), "Hans", "Wurst", Email.of("hw@web.de"));
		referent.makeReferent();
		this.qualification = new Qualification("Erste Hilfe Kurs",
				"Hat die Qualifikation, einen Erste-Hilfe Kurs zu leiten");
	}

	@Test
	public void testAddNullQualification() {
		assertThrows(IllegalArgumentException.class, () ->
			referent.getReferentProfile().addQualification(null));
	}

	@Test
	public void testAddQualification() {
		referent.getReferentProfile().addQualification(qualification);
		Assertions.assertTrue(referent.getReferentProfile().hasQualification(qualification),
				String.format("%s should have qualification %s", referent, qualification));
	}

	@Test
	public void testRemoveQualification() {
		referent.getReferentProfile().addQualification(qualification);
		referent.getReferentProfile().removeQualification(qualification);

		Assertions.assertFalse(
				referent.getReferentProfile().hasQualification(qualification),
				String.format("%s should not have qualification %s any more", referent, qualification));
		Assertions.assertFalse(
				Iterables.contains(referent.getReferentProfile().getQualifications(), qualification),
				String.format("%s should not have qualification %s any more", referent, qualification));
	}

}
