package de.naju.adebar.util.conversion.human;

import de.naju.adebar.controller.forms.human.CreatePersonForm;

import de.naju.adebar.model.human.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

/**
 * Service to extract the necessary data from a 'create person' form
 * @author Rico Bergmann
 * @see CreatePersonForm
 */
@Service
public class CreatePersonFormDataExtractor {

    private DateTimeFormatter dateFormatter;

    public CreatePersonFormDataExtractor() {
        this.dateFormatter = DateTimeFormatter.ofPattern(CreatePersonForm.DATE_FORMAT, Locale.GERMAN);
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link Person} object encoded by the form
     */
    public Person extractPerson(CreatePersonForm personForm) {
        Gender gender = Gender.valueOf(personForm.getGender());
        LocalDate dob = personForm.hasDateOfBirth() ? LocalDate.parse(personForm.getDateOfBirth(), dateFormatter) : null;
        NabuMembership nabu = personForm.isNabuMember() ? new NabuMembership(personForm.getNabuNumber()) : new NabuMembership();
        Person person = new Person(personForm.getFirstName(), personForm.getLastName(), personForm.getEmail(), gender, extractAddress(personForm), dob);
        person.setNabuMembership(nabu);
        person.setEatingHabit(personForm.getEatingHabit());
        person.setHealthImpairments(personForm.getHealthImpairments());
        return person;
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link Address} object encoded by the form
     */
    public Address extractAddress(CreatePersonForm personForm) {
        return new Address(personForm.getStreet(), personForm.getZip(), personForm.getCity());
    }

    /**
     * @param personForm the form containing the data to extract
     * @return an {@link Optional} containing the person's JuLeiCa expiry date if present
     */
    public Optional<LocalDate> extractJuleicaExpiryDate(CreatePersonForm personForm) {
        if (!personForm.isActivist() || !personForm.hasJuleicaExpiryDate()) {
            return Optional.empty();
        } else {
            return Optional.of(LocalDate.parse(personForm.getJuleicaExpiryDate(), dateFormatter));
        }
    }

    /**
     * @param personForm the form containing the data to extract
     * @param qualificationRepo repository containing all available qualifications
     * @return an {@link Optional} containing all qualifications of the person if they are present
     */
    public Optional<Iterable<Qualification>> extractQualifications(CreatePersonForm personForm, QualificationRepository qualificationRepo) {
        if (!personForm.isReferent()) {
            return Optional.empty();
        } else {
            return Optional.of(qualificationRepo.findAll(personForm.getQualifications()));
        }
    }
}
