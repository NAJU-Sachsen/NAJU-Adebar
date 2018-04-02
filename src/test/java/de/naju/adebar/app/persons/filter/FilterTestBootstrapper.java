package de.naju.adebar.app.persons.filter;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rico Bergmann
 */
@Component
public class FilterTestBootstrapper {

  @Autowired
  protected PersonFactory personFactory;

  protected Address hansAddress = new Address("zu Hause 3", "01234", "Nirgends");
  protected Address clausAddress = new Address("Hinter der Boje 7", "55555", "Aufm Meer");
  protected Address bertaAddress = new Address("Bei Mir 1", "98765", "Entenhausen");
  protected Address fritzAddress = new Address("Waseblitzer Straße 11", "03107", "Dersden");
  protected Address heinzAddress = new Address("Am Nischel 42", "01911", "Chmenitz");

  protected LocalDate hansDob = LocalDate.now().minusDays(3L);
  protected LocalDate clausDob = LocalDate.of(1635, 3, 5);
  protected LocalDate bertaDob = LocalDate.now().minusYears(23);
  protected LocalDate fritzDob = LocalDate.now().minusYears(55);
  protected LocalDate heinzDob = bertaDob;

  protected LocalDate hansJuleicaExpiry = LocalDate.of(2017, 2, 1);
  protected LocalDate clausJuleicaExpiry = hansJuleicaExpiry.minusDays(1L);
  protected LocalDate bertaJuleicaExpiry = hansJuleicaExpiry.plusDays(1L);

  protected Qualification bertaQualification1 = new Qualification("Widlife", "");
  protected Qualification bertaQualification2 = new Qualification("Erste-Hilfe Kurs", "");

  // participant and activist
  protected Person hans;

  // participant and activist
  protected Person claus;

  // participant and referent and activist
  protected Person berta;

  // participant and referent
  protected Person fritz;

  // only a camp participant
  protected Person heinz;

  protected List<Person> persons;

  @Autowired
  protected PersonManager personManager;
  @Autowired
  protected PersonRepository personRepo;
  @Autowired
  protected QualificationRepository qualificationRepo;

  @Before
  public void setUp() {
    System.out.println(personRepo.findAll());

    // hans was already created as the application's admin
    hans = personRepo.findByFirstNameAndLastNameAndEmail("Hans", "Wurst",
        Email.of("hans.wurst@web.de"));
    hans.makeParticipant();
    hans.makeActivist();
    hans.updateAddress(hansAddress);
    hans.getParticipantProfile() //
        .updateGender(Gender.MALE) //
        .updateDateOfBirth(hansDob);
    hans.getActivistProfile() //
        .updateJuleicaCard(new JuleicaCard(hansJuleicaExpiry));

    // @formatter:off
    claus = personFactory.buildNew("Claus", "Störtebecker", Email.of("derkaeptn@meermensch.de"))
        .specifyAddress(clausAddress)
        .makeParticipant()
          .specifyGender(Gender.MALE)
          .specifyDateOfBirth(clausDob)
          .done()
        .makeActivist()
          .specifyJuleicaCard(new JuleicaCard(clausJuleicaExpiry))
          .done()
        .create();

    berta = personFactory.buildNew("Berta", "Beate", Email.of("bb@gmx.net"))
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

    fritz = personFactory.buildNew("Fritz", "Käse", Email.of("fritz_kaese@googlemail.com"))
        .specifyAddress(fritzAddress)
        .makeParticipant()
          .specifyDateOfBirth(fritzDob)
          .done()
        .makeReferent()
        .create();

    heinz = personFactory.buildNew("Heinz", "Meinz", Email.of("misterheinz@aol.com"))
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
