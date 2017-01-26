package de.naju.adebar.model.human;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Basic behavior testing for the {@link PersistentActivistManager}
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class ActivistManagerIntegrationTest {
    @Autowired private PersonManager personManager;
    @Autowired private PersistentActivistManager activistManager;
    @Autowired private ActivistRepository activistRepo;
    private PersonId clausId;
    private Person clausPerson;
    private Activist clausActivist;

    @Before public void setUp() {
        this.clausId = new PersonId();
        Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
        LocalDate clausDob = LocalDate.now().minusYears(42L);
        this.clausPerson = new Person(clausId, "Claus", "Störtebecker", "der_kaeptn@web.de",
                Gender.MALE, clausAddress, clausDob);
        this.clausActivist = new Activist(clausId, null);

        personManager.savePerson(clausPerson);
    }

    @Test public void testSaveActivist() {
        activistManager.saveActivist(clausActivist);
        Assert.assertTrue(clausActivist.toString() + " should have been saved!", activistRepo.exists(clausId));
    }

    @Test public void testCreateActivist() {
        activistManager.createActivistForPerson(clausPerson);
        Assert.assertTrue(clausPerson.toString() + " should have been saved!", activistRepo.exists(clausId));
    }

    @Test public void testUpdateActivist() {
        activistManager.createActivistForPerson(clausPerson);
        clausActivist = activistRepo.findOne(clausId);
        clausActivist.setJuleicaExpiryDate(LocalDate.now().plusMonths(12));
        clausActivist = activistManager.updateActivist(clausId, clausActivist);
        Assert.assertTrue(clausActivist.toString() + " should have been updated!", clausActivist.hasJuleica());
    }

    @Test public void testFindActivist() {
        activistManager.saveActivist(clausActivist);
        Assert.assertEquals(clausActivist.toString() + " should have been found!",
                clausActivist, activistManager.findActivistByPerson(clausPerson));
    }

    @Test(expected = NoActivistException.class) public void testFindNoActivist() {
        activistManager.getJuleicaExpiryDateForPerson(clausPerson);
    }

    @Test public void testIsActivist() {
        activistManager.saveActivist(clausActivist);
        Assert.assertTrue(clausPerson.toString() + " is an activist", activistManager.isActivist(clausPerson));

        PersonId bertaId = new PersonId();
        Address bertaAddress = new Address("An der Schiefen Ebene 2", "01234", "Entenhausen", "Zimmer 13");
        LocalDate bertaDob = LocalDate.now().minusYears(27L);
        Person berta = new Person(bertaId, "Berta", "Beate", "berta@gmx.net",
                Gender.FEMALE, bertaAddress, bertaDob);

        Assert.assertFalse(berta.toString() + " is not an activist", activistManager.isActivist(berta));
    }

    @Test public void testGetJuleicaExpiryDate() {
        LocalDate expiryDate = LocalDate.now().plusYears(15);
        clausActivist.setJuleicaExpiryDate(expiryDate);
        activistManager.saveActivist(clausActivist);
        Assert.assertEquals("Should return the expiry date",
                expiryDate, activistManager.getJuleicaExpiryDateForPerson(clausPerson));
    }

    @Test(expected = NoActivistException.class) public void testNoActivistJuleicaExpiryDate() {
        activistManager.getJuleicaExpiryDateForPerson(clausPerson);
    }
}
