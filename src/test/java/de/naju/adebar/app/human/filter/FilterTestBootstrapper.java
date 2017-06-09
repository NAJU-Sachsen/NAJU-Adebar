package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.model.human.*;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @author Rico Bergmann
 */
@Component
public class FilterTestBootstrapper {
    @Autowired PersonFactory personFactory;

    Address hansAddress = new Address("zu Hause 3", "01234", "Nirgends");
    Address clausAddress = new Address("Hinter der Boje 7", "55555", "Aufm Meer");
    Address bertaAddress = new Address("Bei Mir 1", "98765", "Entenhausen");
    Address fritzAddress = new Address("Waseblitzer Straße 11", "03107", "Dersden");
    Address heinzAddress = new Address("Am Nischel 42", "01911", "Chmenitz");

    LocalDate hansDob = LocalDate.now().minusDays(3L);
    LocalDate clausDob = LocalDate.of(1635, 3, 5);
    LocalDate bertaDob = LocalDate.now().minusYears(23);
    LocalDate fritzDob = LocalDate.now().minusYears(55);
    LocalDate heinzDob = bertaDob;

    LocalDate hansJuleicaExpiry = LocalDate.of(2017, 2, 1);
    LocalDate clausJuleicaExpiry = hansJuleicaExpiry.minusDays(1L);
    LocalDate bertaJuleicaExpiry = hansJuleicaExpiry.plusDays(1L);

    Qualification bertaQualification1 = new Qualification("Widlife", "");
    Qualification bertaQualification2 = new Qualification("Erste-Hilfe Kurs", "");

    // participant and activist
    Person hans;

    // participant and activist
    Person claus;

    // participant and referent and activist
    Person berta;

    // participant and referent
    Person fritz;

    // only a camp participant
    Person heinz;

    List<Person> persons;

    @Autowired PersonManager personManager;
    @Autowired PersonRepository personRepo;
    @Autowired QualificationRepository qualificationRepo;

    @Before public void setUp() {
        System.out.println(personRepo.findAll());

        hans = personFactory.buildNew("Hans", "Wurst", "hans.wurst@web.de").makeParticipant().makeActivist().create();
        hans.setAddress(hansAddress);
        hans.getParticipantProfile().setGender(Gender.MALE);
        hans.getParticipantProfile().setDateOfBirth(hansDob);
        hans.getActivistProfile().setJuleicaCard(new JuleicaCard(hansJuleicaExpiry));

        claus = personFactory.buildNew("Claus", "Störtebecker", "derkaeptn@meermensch.de").makeParticipant().makeActivist().create();
        claus.setAddress(clausAddress);
        claus.getParticipantProfile().setGender(Gender.MALE);
        claus.getParticipantProfile().setDateOfBirth(clausDob);
        claus.getActivistProfile().setJuleicaCard(new JuleicaCard(clausJuleicaExpiry));

        berta = personFactory.buildNew("Berta", "Beate", "bb@gmx.net").makeParticipant().makeActivist().makeReferent().create();
        berta.setAddress(bertaAddress);
        berta.getParticipantProfile().setGender(Gender.FEMALE);
        berta.getParticipantProfile().setDateOfBirth(bertaDob);
        berta.getActivistProfile().setJuleicaCard(new JuleicaCard(bertaJuleicaExpiry));
        berta.getReferentProfile().addQualification(bertaQualification1);
        berta.getReferentProfile().addQualification(bertaQualification2);

        fritz = personFactory.buildNew("Fritz", "Käse", "fritz_kaese@googlemail.com").makeParticipant().makeReferent().create();
        fritz.setAddress(fritzAddress);
        fritz.getParticipantProfile().setDateOfBirth(fritzDob);

        heinz = personFactory.buildNew("Heinz", "Meinz", "misterheinz@aol.com").makeParticipant().create();
        heinz.setAddress(heinzAddress);
        heinz.getParticipantProfile().setGender(Gender.MALE);
        heinz.getParticipantProfile().setDateOfBirth(heinzDob);

        persons = Arrays.asList(hans, claus, berta, fritz, heinz);

        for (Person p : persons) {
            personManager.savePerson(p);
        }

    }
}
