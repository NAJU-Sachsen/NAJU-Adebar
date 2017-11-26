package de.naju.adebar.app.human;

import de.naju.adebar.model.human.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic behavior testing of the {@link PersonManager} regarding {@link ReferentProfile}
 * 
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Component
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

  @Before
  public void setUp() {
    Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
    this.claus = personFactory.buildNew("Claus", "Störtebecker", "der_kaeptn@web.de").makeReferent()
        .create();
    this.claus.setAddress(clausAddress);
    this.qualification = new Qualification("Erste Hilfe Kurs",
        "Hat die Qualifikation, einen Erste-Hilfe Kurs zu leiten");

    this.clausId = claus.getId();
  }

  @Test
  public void testSaveReferent() {
    personManager.savePerson(claus);
    Assert.assertTrue(claus.toString() + " should have been saved!", referentRepo.exists(clausId));
  }

  @Test
  public void testUpdateReferent() {
    claus = personManager.savePerson(claus);
    claus.getReferentProfile().addQualification(qualification);
    claus = personManager.updatePerson(clausId, claus);
    Assert.assertTrue(claus.toString() + " should have been updated!",
        claus.getReferentProfile().hasQualification(qualification));
  }

  @Test
  public void testIsReferent() {
    personManager.savePerson(claus);
    Person berta = personFactory.buildNew("Berta", "Beate", "berta@gmx.net").create();
    personManager.savePerson(berta);

    Assert.assertFalse(berta.toString() + " is not an activist",
        referentRepo.exists(berta.getId()));
  }
}
