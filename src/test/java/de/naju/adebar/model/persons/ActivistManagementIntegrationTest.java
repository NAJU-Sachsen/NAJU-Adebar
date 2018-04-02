package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.details.JuleicaCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic behavior testing for the {@link PersistentPersonManager} and its behavior on {@link
 * ActivistProfile} instances
 *
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
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

  @Before
  public void setUp() {
    Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
    this.claus = personFactory.buildNew("Claus", "Störtebecker", Email.of("der_kaeptn@web.de"))
        .makeActivist().create();
    claus.setAddress(clausAddress);

    personManager.savePerson(claus);
  }

  @Test
  public void testSaveActivist() {
    Assert.assertTrue(claus.toString() + " should have been saved!",
        activistRepo.existsById(claus.getId()));
  }

  @Test
  public void testUpdateActivist() {

    // update the person
    claus = personManager.repository().findEntry(claus.getId());
    claus.getActivistProfile().setJuleicaCard(new JuleicaCard());

    // save the updated person
    claus = personManager.updatePerson(claus);

    Assert.assertTrue("Person should have Juleica card", claus.getActivistProfile().hasJuleica());
  }

}
