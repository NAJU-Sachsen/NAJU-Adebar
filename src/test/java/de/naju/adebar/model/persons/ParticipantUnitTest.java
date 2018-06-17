package de.naju.adebar.model.persons;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.core.Email;

/**
 * @author Rico Bergmann
 */
public class ParticipantUnitTest {

  private Person hans;

  @Before
  public void setUp() {
    hans = new Person(new PersonId(), "Hans", "Wurst", Email.of("hans@web.de"));
    hans.makeParticipant();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateOfBirth() {
    hans.getParticipantProfile().setDateOfBirth(LocalDate.MAX);
  }

  @Test
  public void testCalculateAge() {
    LocalDate dob = LocalDate.now().minusDays(1);
    hans.getParticipantProfile().setDateOfBirth(dob);

    Age age = Age.of(0);
    Assert.assertEquals(age, hans.getParticipantProfile().calculateAge());

    age = Age.of(5);
    hans.getParticipantProfile().setDateOfBirth(dob.minusYears(age.getValue()));
    Assert.assertEquals(age, hans.getParticipantProfile().calculateAge());

    age = Age.of(42);
    hans.getParticipantProfile().setDateOfBirth(dob.minusYears(age.getValue()));
    Assert.assertEquals(age, hans.getParticipantProfile().calculateAge());

  }
}
