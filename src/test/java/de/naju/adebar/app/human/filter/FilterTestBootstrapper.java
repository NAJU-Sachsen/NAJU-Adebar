package de.naju.adebar.app.human.filter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.model.persons.Address;
import de.naju.adebar.model.persons.Gender;
import de.naju.adebar.model.persons.JuleicaCard;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.Qualification;
import de.naju.adebar.model.persons.QualificationRepository;

/**
 * @author Rico Bergmann
 */
@Component
public class FilterTestBootstrapper {
  @Autowired
  PersonFactory personFactory;

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

  @Autowired
  PersonManager personManager;
  @Autowired
  PersonRepository personRepo;
  @Autowired
  QualificationRepository qualificationRepo;

  @Before
  public void setUp() {
    System.out.println(personRepo.findAll());

    // hans was already created as the application's admin
    hans = personRepo.findByFirstNameAndLastNameAndEmail("Hans", "Wurst", "hans.wurst@web.de");
    hans.makeParticipant();
    hans.makeActivist();
    hans.updateAddress(hansAddress);
    hans.getParticipantProfile() //
        .updateGender(Gender.MALE) //
        .updateDateOfBirth(hansDob);
    hans.getActivistProfile() //
        .updateJuleicaCard(new JuleicaCard(hansJuleicaExpiry));

    // @formatter:off
    claus = personFactory.buildNew("Claus", "Störtebecker", "derkaeptn@meermensch.de")
        .specifyAddress(clausAddress)
        .makeParticipant()
          .specifyGender(Gender.MALE)
          .specifyDateOfBirth(clausDob)
          .done()
        .makeActivist()
          .specifyJuleicaCard(new JuleicaCard(clausJuleicaExpiry))
          .done()
        .create();

    berta = personFactory.buildNew("Berta", "Beate", "bb@gmx.net")
        .specifyAddress(bertaAddress)
        .makeParticipant()
          .specifyGender(Gender.FEMALE)
          .specifyDateOfBirth(bertaDob)
          .done()
        .makeActivist()
          .specifyJuleicaCard(new JuleicaCard(bertaJuleicaExpiry))
          .done()
        .makeReferent()
          .specifyQualifications(Arrays.asList(bertaQualification1, bertaQualification2))
        .create();

    fritz = personFactory.buildNew("Fritz", "Käse", "fritz_kaese@googlemail.com")
        .specifyAddress(fritzAddress)
        .makeParticipant()
          .specifyDateOfBirth(fritzDob)
          .done()
        .makeReferent()
        .create();

    heinz = personFactory.buildNew("Heinz", "Meinz", "misterheinz@aol.com")
        .specifyAddress(heinzAddress)
        .makeParticipant()
          .specifyGender(Gender.MALE)
          .specifyDateOfBirth(heinzDob)
        .create();

    // @formatter:on

    persons = Arrays.asList(hans, claus, berta, fritz, heinz);

    for (Person p : persons) {
      personManager.savePerson(p);
    }

  }
}
