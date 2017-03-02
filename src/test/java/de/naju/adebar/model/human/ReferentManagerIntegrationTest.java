package de.naju.adebar.model.human;

import com.google.common.collect.Iterables;
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
 * Basic behavior testing of the {@link PersistentReferentManager}
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class ReferentManagerIntegrationTest {

    @Autowired private PersonManager personManager;
    @Autowired private PersistentReferentManager referentManager;
    @Autowired private ReferentRepository referentRepo;
    @Autowired private QualificationRepository qualificationRepo;
    private PersonId clausId;
    private Person clausPerson;
    private Referent clausReferent;
    private Qualification qualification;

    @Before public void setUp() {
        this.clausId = new PersonId();
        Address clausAddress = new Address("Hinner der Boje 7", "24103", "Auf'm Meer");
        LocalDate clausDob = LocalDate.now().minusYears(42L);
        this.clausPerson = new Person(clausId, "Claus", "Störtebecker", "der_kaeptn@web.de",
                Gender.MALE, clausAddress, clausDob, new NabuMembership());
        this.clausReferent = new Referent(clausId);
        this.qualification = new Qualification("Erste Hilfe Kurs",
                "Hat die Qualifikation, einen Erste-Hilfe Kurs zu leiten");

        personManager.savePerson(clausPerson);
        qualificationRepo.save(qualification);
    }

    @Test public void testSaveReferent() {
        referentManager.saveReferent(clausReferent);
        Assert.assertTrue(clausReferent.toString() + " should have been saved!", referentRepo.exists(clausId));
    }

    @Test public void testCreateReferent() {
        referentManager.createReferentForPerson(clausPerson);
        Assert.assertTrue(clausPerson.toString() + " should have been saved!", referentRepo.exists(clausId));
    }

    @Test public void testUpdateReferent() {
        referentManager.createReferentForPerson(clausPerson);
        clausReferent = referentRepo.findOne(clausId);
        clausReferent.addQualification(qualification);
        clausReferent = referentManager.updateReferent(clausId, clausReferent);
        Assert.assertTrue(clausReferent.toString() + " should have been updated!",
                clausReferent.hasQualification(qualification));
    }

    @Test public void testFindReferent() {
        referentManager.saveReferent(clausReferent);
        Assert.assertEquals(clausReferent.toString() + " should have been returned!", clausReferent,
                referentManager.findReferentByPerson(clausPerson));
    }

    @Test(expected = NoReferentException.class) public void testFindNoReferent() {
        referentManager.findReferentByPerson(clausPerson);
    }

    @Test public void testIsReferent() {
        referentManager.saveReferent(clausReferent);
        Assert.assertTrue(clausPerson.toString() + " is an referent!", referentManager.isReferent(clausPerson));

        PersonId bertaId = new PersonId();
        Address bertaAddress = new Address("An der Schiefen Ebene 2", "01234", "Entenhausen", "Zimmer 13");
        LocalDate bertaDob = LocalDate.now().minusYears(27L);
        Person berta = new Person(bertaId, "Berta", "Beate", "berta@gmx.net",
                Gender.FEMALE, bertaAddress, bertaDob, new NabuMembership());

        Assert.assertFalse(berta.toString() + " is not an activist", referentManager.isReferent(berta));
    }

    @Test public void testGetQualifications() {
        clausReferent.addQualification(qualification);
        Qualification[] qualifications = Iterables.toArray(clausReferent.getQualifications(), Qualification.class);
        referentManager.saveReferent(clausReferent);
        Assert.assertArrayEquals("Should return the qualifications", qualifications,
                Iterables.toArray(referentManager.getQualificationsForPerson(clausPerson), Qualification.class));
    }

    @Test(expected = NoReferentException.class) public void testGetQualificationsNoReferent() {
        referentManager.getQualificationsForPerson(clausPerson);
    }
}
