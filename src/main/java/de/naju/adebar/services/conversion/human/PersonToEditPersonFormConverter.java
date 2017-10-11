package de.naju.adebar.services.conversion.human;

import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.ParticipantProfile;
import de.naju.adebar.model.human.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to convert a {@link Person} to a corresponding {@link EditPersonForm}
 * @author Rico Bergmann
 */
@Service
public class PersonToEditPersonFormConverter {

    /**
     * Performs the conversion
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
        String dob = d != null ? d.format(DateTimeFormatter.ofPattern(EditPersonForm.DATE_FORMAT, Locale.GERMAN)) : null;

        personForm.setParticipant(true);
        personForm.setGender(profile.getGender() != null ? profile.getGender().toString() : null);
        personForm.setDateOfBirth(dob);
        personForm.setEatingHabit(profile.getEatingHabits());
        personForm.setHealthImpairments(profile.getHealthImpairments());
        personForm.setRemarks(profile.getRemarks());
        
        if (profile.isNabuMember()) {
            personForm.setNabuMember(true);
            personForm.setNabuNumber(profile.getNabuMembership().getMembershipNumber());
        }
    }

}
