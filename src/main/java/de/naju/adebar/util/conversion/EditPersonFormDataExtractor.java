package de.naju.adebar.util.conversion;

import de.naju.adebar.controller.forms.CreatePersonForm;
import de.naju.adebar.controller.forms.EditPersonForm;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Gender;
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
        Person person = new Person(personForm.getFirstName(), personForm.getLastName(), personForm.getEmail(), Gender.valueOf(personForm.getGender()),
                new Address(personForm.getStreet(), personForm.getZip(), personForm.getCity()), LocalDate.parse(personForm.getDateOfBirth(), dateFormatter));
        person.setEatingHabit(personForm.getEatingHabit());
        person.setHealthImpairments(personForm.getHealthImpairments());
        return person;
    }

}
