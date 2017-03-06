package de.naju.adebar.util.conversion.human;

import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.model.human.Person;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to convert a {@link Person} to a corresponding {@link EditPersonForm}
 * @author Rico Bergmann
 */
@Service
public class PersonToEditPersonFormConverter {

    public EditPersonForm convertToEditPersonForm(Person person) {
        String dob = person.hasDateOfBirth() ? person.getDateOfBirth().format(DateTimeFormatter.ofPattern(EditPersonForm.DATE_FORMAT, Locale.GERMAN)) : "";
        return new EditPersonForm(person.getFirstName(), person.getLastName(), person.getEmail(), person.getGender().name(), dob, person.getEatingHabit(),person.getHealthImpairments(), person.getAddress().getStreet(), person.getAddress().getZip(), person.getAddress().getCity(), person.getNabuMembership().isNabuMember(), person.getNabuMembership().getMembershipNumber());
    }

}
