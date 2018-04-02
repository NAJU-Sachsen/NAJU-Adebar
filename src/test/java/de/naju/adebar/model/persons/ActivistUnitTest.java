package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.details.JuleicaCard;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic testing of the {@link ActivistProfile} class
 *
 * @author Rico Bergmann
 */
public class ActivistUnitTest {

  private Person hans;

  @Before
  public void setUp() {
    hans = new Person(new PersonId(), "Hans", "Wurst", Email.of("hans@web.de"));
    hans.makeActivist();
  }

  @Test
  public void testHasJuleica() {
    hans.getActivistProfile().setJuleicaCard(new JuleicaCard(LocalDate.now()));
    Assert.assertTrue("Activist should have a JuLeiCa card",
        hans.getActivistProfile().hasJuleica());
  }

  @Test
  public void testHasNoJuleica() {
    Assert.assertFalse("Activist should have no JuLeiCa card",
        hans.getActivistProfile().hasJuleica());
  }

  @Test
  public void testRemoveJuleica() {
    hans.getActivistProfile().setJuleicaCard(new JuleicaCard(LocalDate.now()));
    hans.getActivistProfile().setJuleicaCard(null);
    Assert.assertFalse("Activist should have no JuLeiCa card",
        hans.getActivistProfile().hasJuleica());
  }

}
