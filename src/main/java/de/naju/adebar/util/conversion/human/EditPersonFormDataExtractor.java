package de.naju.adebar.util.conversion.human;

import de.naju.adebar.controller.forms.human.CreatePersonForm;
import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Gender;
import de.naju.adebar.model.human.NabuMembership;
import de.naju.adebar.model.human.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to extract the necessary data from an 'edit person' form
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
     * @param personForm the form containing the data to extract
     * @return the {@link Person} object encoded by the form
     */
    public Person extractPerson(EditPersonForm personForm) {
        LocalDate dob = personForm.hasDateOfBirth() ? LocalDate.parse(personForm.getDateOfBirth(), dateFormatter) : null;
        Person person = new Person(personForm.getFirstName(), personForm.getLastName(), personForm.getEmail(), personForm.getPhoneNumber(), Gender.valueOf(personForm.getGender()), new Address(personForm.getStreet(), personForm.getZip(), personForm.getCity()), dob);
        person.setEatingHabit(personForm.getEatingHabit());
        person.setHealthImpairments(personForm.getHealthImpairments());
        person.setNabuMembership(personForm.isNabuMember() ? new NabuMembership(personForm.getNabuNumber()) : new NabuMembership());
        return person;
    }

}
