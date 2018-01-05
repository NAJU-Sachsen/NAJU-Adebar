package de.naju.adebar.services.conversion.human;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;
import de.naju.adebar.controller.forms.human.CreatePersonForm;
import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Gender;
import de.naju.adebar.model.human.NabuMembershipInformation;
import de.naju.adebar.model.human.ParticipantProfile;
import de.naju.adebar.model.human.Person;

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
    Person updated = person.updateInformation( //
        personForm.getFirstName(), //
        personForm.getLastName(), //
        personForm.getEmail(), //
        personForm.getPhoneNumber()) //
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
    switch (personForm.getNabuMember()) {
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
        throw new AssertionError(personForm.getNabuMember());
    }

    return profile
        .updateProfile(gender, dob, personForm.getEatingHabit(), personForm.getHealthImpairments())
        .updateNabuMembership(nabuMembershipInformation).updateRemarks(personForm.getRemarks());
  }

}
