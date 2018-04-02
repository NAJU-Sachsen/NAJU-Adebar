package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.details.Gender;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic testing of the {@link PersistentPersonManager}
 *
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PersistentPersonManagerIntegrationTest {

  @Autowired
  PersonFactory personFactory;
  @Autowired
  private PersonRepository personRepo;
  @Autowired
  private PersistentPersonManager personManager;
  private PersonId bertaId;
  private Person claus, berta;
  private Address bertaAddress;
  private LocalDate heinzDob;

  @Before
  public void setUp() {
    Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
    LocalDate clausDob = LocalDate.now().minusYears(42L);
    this.claus = personFactory.buildNew("Claus", "Störtebecker", Email.of("der_kaeptn@web.de"))
        .makeParticipant().create();
    claus.getParticipantProfile().setGender(Gender.MALE);
    claus.setAddress(clausAddress);
    claus.getParticipantProfile().setDateOfBirth(clausDob);

    this.bertaAddress = new Address("An der Schiefen Ebene 2", "01234", "Entenhausen", "Zimmer 13");
    LocalDate bertaDob = LocalDate.now().minusYears(27L);
    this.berta = personFactory.buildNew("Berta", "Beate", Email.of("berta@gmx.net"))
        .makeParticipant().create();
    berta.setAddress(bertaAddress);
    berta.getParticipantProfile().setGender(Gender.FEMALE);
    berta.getParticipantProfile().setDateOfBirth(bertaDob);

    this.bertaId = berta.getId();

    new Address("Zu Hause", "98765", "Teudschland");
    this.heinzDob = LocalDate.now().minusYears(3);
  }

  @Test
  public void testSavePerson() {
    claus = personManager.savePerson(claus);
    berta = personManager.savePerson(berta);

    Assert.assertTrue(claus.toString() + " should have been saved!",
        personRepo.existsById(claus.getId()));
    Assert
        .assertTrue(berta.toString() + " should have been saved!", personRepo.existsById(bertaId));
  }

  @Test
  public void testCreatePerson() {
    Person heinz =
        personManager.createPerson("Heinz", "Der Heinz", Email.of("heeeiiiinnnzzzzz@gmail.com"));
    Assert.assertTrue(heinz.toString() + " should have been saved!",
        personRepo.existsById(heinz.getId()));
  }

  @Test
  public void testUpdatePerson() {
    claus = personManager.savePerson(claus);
    claus.setAddress(bertaAddress);
    claus.getParticipantProfile().setDateOfBirth(heinzDob);
    claus = personManager.updatePerson(claus);
    Assert.assertEquals("Address should have been updated!", bertaAddress, claus.getAddress());
    Assert.assertEquals("Dob should have been updated!", heinzDob,
        claus.getParticipantProfile().getDateOfBirth());
  }

}
