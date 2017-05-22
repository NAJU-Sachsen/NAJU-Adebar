package de.naju.adebar.util.conversion.human;

import de.naju.adebar.controller.forms.human.CreatePersonForm;
import de.naju.adebar.controller.forms.human.EditActivistForm;
import de.naju.adebar.controller.forms.human.EditPersonForm;
import de.naju.adebar.model.human.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private PersonFactory personFactory;

    private DateTimeFormatter dateFormatter;

    @Autowired
    public EditPersonFormDataExtractor(PersonFactory personFactory) {
        Assert.notNull(personFactory, "Person factory may not be null");
        this.personFactory = personFactory;
        this.dateFormatter = DateTimeFormatter.ofPattern(CreatePersonForm.DATE_FORMAT, Locale.GERMAN);
    }

    /**
     * @param id the ID of the person to extract
     * @param personForm the form containing the data to extract
     * @return the {@link Person} object encoded by the form
     */
    public Person extractPerson(PersonId id, EditPersonForm personForm) {
        Person person = personFactory.buildNew(personForm.getFirstName(), personForm.getLastName(), personForm.getEmail()).create();

        setId(person, id);
        person.setPhoneNumber(personForm.getPhoneNumber());
        person.setAddress(new Address(personForm.getStreet(), personForm.getZip(), personForm.getCity()));

        if (personForm.isParticipant()) {
            extractParticipantProfile(person, personForm);
        }

        return person;
    }

    public void extractParticipantProfile(Person person, EditPersonForm personForm) {
        ParticipantProfile profile = person.makeParticipant();
        Gender gender = Gender.valueOf(personForm.getGender());
        LocalDate dob = personForm.hasDateOfBirth() ? LocalDate.parse(personForm.getDateOfBirth(), dateFormatter) : null;

        profile.setGender(gender);
        profile.setDateOfBirth(dob);
        profile.setEatingHabits(personForm.getEatingHabit());
        profile.setHealthImpairments(personForm.getHealthImpairments());
        profile.setRemarks(personForm.getRemarks());
        profile.setNabuMembership(personForm.isNabuMember() ? new NabuMembership(personForm.getNabuNumber()) : new NabuMembership());
    }

    /**
     * Sets the ID of a person
     * @param p the person to update
     * @param id the ID to set
     */
    private void setId(Person p, PersonId id) {
        try {
            Method changeId = Person.class.getDeclaredMethod("setId", PersonId.class);
            changeId.setAccessible(true);
            changeId.invoke(p, id);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error during invocation of reflection", e);
        }
    }

}
