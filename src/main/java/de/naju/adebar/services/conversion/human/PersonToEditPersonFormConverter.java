package de.naju.adebar.services.conversion.human;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;
import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.model.persons.Address;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;

/**
 * Service to convert a {@link Person} to a corresponding {@link EditPersonForm}
 * 
 * @author Rico Bergmann
 */
@Service
public class PersonToEditPersonFormConverter {

  /**
   * Performs the conversion
   * 
   * @param person the person to convert
   * @return the created form
   */
  public EditPersonForm convertToEditPersonForm(Person person) {
    EditPersonForm personForm = new EditPersonForm();
    fillPersonData(personForm, person);

    if (person.isParticipant()) {
      fillParticipantData(personForm, person.getParticipantProfile());
    }

    return personForm;
  }

  protected void fillPersonData(EditPersonForm personForm, Person person) {
    personForm.setFirstName(person.getFirstName());
    personForm.setLastName(person.getLastName());
    personForm.setEmail(person.getEmail());
    personForm.setPhoneNumber(person.getPhoneNumber());

    Address address = person.getAddress();
    if (address != null) {
      personForm.setStreet(address.getStreet());
      personForm.setZip(address.getZip());
      personForm.setCity(address.getCity());
    }
  }

  protected void fillParticipantData(EditPersonForm personForm, ParticipantProfile profile) {
    LocalDate d = profile.getDateOfBirth();
    String dob = d != null //
        ? d.format(DateTimeFormatter.ofPattern(EditPersonForm.DATE_FORMAT, Locale.GERMAN)) //
        : null;

    personForm.setParticipant(true);
    personForm.setGender(profile.getGender() != null ? profile.getGender().toString() : null);
    personForm.setDateOfBirth(dob);
    personForm.setEatingHabit(profile.getEatingHabits());
    personForm.setHealthImpairments(profile.getHealthImpairments());
    personForm.setRemarks(profile.getRemarks());

    if (profile.isNabuMember()) {
      personForm.setNabuMember(EditPersonForm.NabuMembershipStatus.IS_MEMBER);
      personForm.setNabuNumber(profile.getNabuMembership().getMembershipNumber());
    } else if (profile.isNabuMembershipUnknown()) {
      personForm.setNabuMember(EditPersonForm.NabuMembershipStatus.UNKNOWN);
    } else {
      personForm.setNabuMember(EditPersonForm.NabuMembershipStatus.NO_MEMBER);
    }
  }

}
