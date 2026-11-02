package de.naju.adebar.model.persons;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.naju.adebar.model.core.Age;

import static org.junit.jupiter.api.Assertions.assertThrows;
import de.naju.adebar.model.core.Email;

/**
 * @author Rico Bergmann
 */
public class ParticipantUnitTest {

	private Person hans;

	@BeforeEach
	public void setUp() {
		hans = new Person(new PersonId(), "Hans", "Wurst", Email.of("hans@web.de"));
		hans.makeParticipant();
	}

	@Test
	public void testInvalidDateOfBirth() {
		assertThrows(IllegalArgumentException.class, () ->
			hans.getParticipantProfile().setDateOfBirth(LocalDate.MAX));
	}

	@Test
	public void testCalculateAge() {
		LocalDate dob = LocalDate.now().minusDays(1);
		hans.getParticipantProfile().setDateOfBirth(dob);

		Age age = Age.of(0);
		Assertions.assertEquals(age, hans.getParticipantProfile().calculateAge());

		age = Age.of(5);
		hans.getParticipantProfile().setDateOfBirth(dob.minusYears(age.getValue()));
		Assertions.assertEquals(age, hans.getParticipantProfile().calculateAge());

		age = Age.of(42);
		hans.getParticipantProfile().setDateOfBirth(dob.minusYears(age.getValue()));
		Assertions.assertEquals(age, hans.getParticipantProfile().calculateAge());

	}
}
