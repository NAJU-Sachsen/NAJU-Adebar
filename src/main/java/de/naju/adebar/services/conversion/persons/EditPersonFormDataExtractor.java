package de.naju.adebar.services.conversion.persons;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;
import de.naju.adebar.controller.forms.persons.CreatePersonForm;
import de.naju.adebar.controller.forms.persons.EditPersonForm;
import de.naju.adebar.model.Address;
import de.naju.adebar.model.Email;
import de.naju.adebar.model.PhoneNumber;
import de.naju.adebar.model.persons.Gender;
import de.naju.adebar.model.persons.NabuMembershipInformation;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;

/**
 * Service to extract the necessary data from an 'edit person' form
 *
 * @author Rico Bergmann
 * @see EditPersonForm
 */
@Service
public class EditPersonFormDataExtractor {

  private DateTimeFormatter dateFormatter;

  public EditPersonFormDataExtractor() {
    this.dateFormatter = DateTimeFormatter.ofPattern(CreatePersonForm.DATE_FORMAT, Locale.GERMAN);
  }

  /**
   * @param id the ID of the person to extract
   * @param personForm the form containing the data to extract
   * @return the {@link Person} object encoded by the form
   */
  public Person updatePerson(Person person, EditPersonForm personForm) {
    Person updated = person //
        .updateInformation( //
            personForm.getFirstName(), //
            personForm.getLastName(), //
            Email.of(personForm.getEmail()), //
            PhoneNumber.of(personForm.getPhoneNumber())) //
        .updateAddress( //
            new Address(personForm.getStreet(), personForm.getZip(), personForm.getCity()));

    if (personForm.isParticipant()) {

      // if the person is not known to be an activist already, make it one
      if (!updated.isParticipant()) {
        updated.makeParticipant();
      }

      updateParticipantProfile(updated.getParticipantProfile(), personForm);
    }

    return updated;
  }

  /**
   * @param profile the profile to update
   * @param personForm the form to extract the new data from
   * @return the updated profile
   */
  public ParticipantProfile updateParticipantProfile(ParticipantProfile profile,
      EditPersonForm personForm) {
    Gender gender = Gender.valueOf(personForm.getGender());

    LocalDate dob = personForm.hasDateOfBirth() //
        ? LocalDate.parse(personForm.getDateOfBirth(), dateFormatter) //
        : null;

    NabuMembershipInformation nabuMembershipInformation = null;
    switch (personForm.getNabuMembershipStatus()) {
      case IS_MEMBER:
        nabuMembershipInformation = new NabuMembershipInformation(personForm.getNabuNumber());
        break;
      case NO_MEMBER:
        nabuMembershipInformation = new NabuMembershipInformation(false);
        break;
      case UNKNOWN:
        nabuMembershipInformation = null;
        break;
      default:
        throw new AssertionError(personForm.getNabuMembershipStatus());
    }

    return profile
        .updateProfile(gender, dob, personForm.getEatingHabit(), personForm.getHealthImpairments())
        .updateNabuMembership(nabuMembershipInformation).updateRemarks(personForm.getRemarks());
  }

}
