package de.naju.adebar.app.human.filter;

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

    // activist
    Person hans = new Person("Hans", "Wurst", "hans.wurst@web.de", "", Gender.MALE, hansAddress, hansDob);

    // activist
    Person claus = new Person("Claus", "Störtebecker", "derkaeptn@meermensch.de", "", Gender.MALE, clausAddress, clausDob);

    // referent and activist
    Person berta = new Person("Berta", "Beate", "bb@gmx.net", "", Gender.FEMALE, bertaAddress, bertaDob);

    // referent
    Person fritz = new Person("Fritz", "Käse", "fritz_kaese@googlemail.com", "", Gender.OTHER, fritzAddress, fritzDob);

    // only a camp participant
    Person heinz = new Person("Heinz", "Meinz", "misterheinz@aol.com", "", Gender.MALE, heinzAddress, heinzDob);

    Activist hansActivist, clausActivist, bertaActivist;

    List<Person> persons = Arrays.asList(hans, claus, berta, fritz, heinz);

    @Autowired PersonManager personManager;
    @Autowired ActivistManager activistManager;
    @Autowired ReferentManager referentManager;
    @Autowired PersonRepository personRepo;
    @Autowired ActivistRepository activistRepo;
    @Autowired ReferentRepository referentRepo;
    @Autowired QualificationRepository qualificationRepo;

    @Before public void setUp() {
        for (Person p : persons) {
            personManager.savePerson(p);
        }

        hansActivist = activistManager.createActivistForPerson(hans);
        clausActivist = activistManager.createActivistForPerson(claus);
        bertaActivist = activistManager.createActivistForPerson(berta);

        hansActivist.setJuleicaExpiryDate(hansJuleicaExpiry);
        bertaActivist.setJuleicaExpiryDate(bertaJuleicaExpiry);
        clausActivist.setJuleicaExpiryDate(clausJuleicaExpiry);

        activistRepo.save(Arrays.asList(hansActivist, bertaActivist, clausActivist));

        Referent bertaReferent = referentManager.createReferentForPerson(berta);
        referentManager.createReferentForPerson(fritz);

        qualificationRepo.save(bertaQualification1);
        qualificationRepo.save(bertaQualification2);
        bertaReferent.addQualification(bertaQualification1);
        bertaReferent.addQualification(bertaQualification2);
        referentRepo.save(bertaReferent);

    }
}
