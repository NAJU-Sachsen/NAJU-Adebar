package de.naju.adebar.model.persons;

import static org.assertj.core.api.Assertions.assertThat;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.model.persons.exceptions.ArchivedPersonException;
import de.naju.adebar.model.persons.qualifications.Qualification;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic testing of the {@link Person} class
 *
 * @author Rico Bergmann
 */
public class PersonUnitTest {

  private Person person;

  @Before
  public void setUp() {
    person = new Person(new PersonId(), "Hans", "Wurst", Email.of("hans.wurst@web.de"));
    person.setAddress(Address.of("Hauptstraße 3", "01234", "Entehausen"));
  }

  @Test
  public void personMayBeMadeParticipantRetrospectively() {
    assertThat(person.isParticipant()).isFalse();

    person.makeParticipant();
    assertThat(person.isParticipant()).isTrue();
    assertThat(person.getParticipantProfile()).isNotNull();
  }

  @Test
  public void usesProvidedParticipantInformationWhenMadeParticipant() {
    LocalDate dateOfBirth = LocalDate.of(1999, 11, 22);
    person.makeParticipant(Gender.FEMALE, dateOfBirth, "vegan", "");
    assertThat(person.getParticipantProfile().getGender()).isEqualTo(Gender.FEMALE);
    assertThat(person.getParticipantProfile().getDateOfBirth()).isEqualTo(dateOfBirth);
    assertThat(person.getParticipantProfile().getEatingHabits()).isEqualTo("vegan");
  }

  @Test(expected = ArchivedPersonException.class)
  public void personMayNotBeMadeParticipantIfArchived() {
    person.archive();
    person.makeParticipant();
  }

  @Test
  public void personMayBeMadeActivistRetrospectively() {
    assertThat(person.isActivist()).isFalse();

    person.makeActivist();
    assertThat(person.isActivist()).isTrue();
    assertThat(person.getActivistProfile()).isNotNull();
  }

  @Test
  public void usesProvidedActivistInformationWhenMadeActivst() {
    LocalDate expiryDate = LocalDate.of(2222, 3, 11);
    JuleicaCard juleica = new JuleicaCard(expiryDate, "L");
    person.makeActivist(juleica);
    assertThat(person.getActivistProfile().getJuleicaCard()).isEqualTo(juleica);
  }

  @Test(expected = ArchivedPersonException.class)
  public void personMayNotBeMadeActivistIfArchived() {
    person.archive();
    person.makeActivist();
  }

  @Test
  public void personMayBeMadeReferentRetrospectively() {
    assertThat(person.isReferent()).isFalse();

    person.makeReferent();
    assertThat(person.isReferent()).isTrue();
    assertThat(person.getReferentProfile()).isNotNull();
  }

  @Test
  public void usesProvidedReferentInformationWhenMadeReferent() {
    Collection<Qualification> qualifications = Arrays.asList( //
        new Qualification("Very important", "Lorem ipsum"), //
        new Qualification("Event more important", "et dolor sit amet"));
    person.makeReferent(qualifications);

    assertThat(person.getReferentProfile().getQualifications())
        .containsExactlyElementsOf(qualifications);
  }

  @Test
  public void archiveRemovesPersonalInformation() {
    person.archive();
    assertThat(person.getFirstName()).isEmpty();
    assertThat(person.getEmail()).isNull();
    assertThat(person.getAddress().getStreet()).isEmpty();
  }

  @Test(expected = ArchivedPersonException.class)
  public void mayOnlyBeArchivedOnce() {
    person.archive();
    person.archive();
  }

  @Test
  public void updateInformationPublishesEvent() {
    person.updateInformation("John", "Doe", Email.of("doejohn@mail.com"),
        PhoneNumber.of("0123 45678901"));
  }

}
