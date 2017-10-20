package de.naju.adebar.services.conversion.human;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import de.naju.adebar.controller.forms.human.CreateParentForm;
import de.naju.adebar.controller.forms.human.CreatePersonForm;
import de.naju.adebar.model.human.ActivistProfile;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Gender;
import de.naju.adebar.model.human.JuleicaCard;
import de.naju.adebar.model.human.NabuMembership;
import de.naju.adebar.model.human.ParticipantProfile;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonFactory;
import de.naju.adebar.model.human.Qualification;
import de.naju.adebar.model.human.QualificationRepository;
import de.naju.adebar.model.human.ReferentProfile;

// TODO make CreatePersonFormDataExtractor subclass of EditPersonFormDataExtractor

/**
 * Service to extract the necessary data from a 'create person' form
 * @author Rico Bergmann
 * @see CreatePersonForm
 */
@Service
public class CreatePersonFormDataExtractor {
    private PersonFactory personFactory;
    private QualificationRepository qualificationRepo;
    private DateTimeFormatter dateFormatter;

    @Autowired
    public CreatePersonFormDataExtractor(PersonFactory personFactory, QualificationRepository qualificationRepo) {
        Object[] params = {personFactory, qualificationRepo};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.personFactory = personFactory;
        this.qualificationRepo = qualificationRepo;
        this.dateFormatter = DateTimeFormatter.ofPattern(CreatePersonForm.DATE_FORMAT, Locale.GERMAN);
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link Person} object encoded by the form
     */
    public Person extractPerson(CreatePersonForm personForm) {
        Person person = personFactory.buildNew(personForm.getFirstName(), personForm.getLastName(), personForm.getEmail()).create();
        person.setPhoneNumber(personForm.getPhoneNumber());
        person.setAddress(extractAddress(personForm));

        if (personForm.isParticipant()) {
            person.makeParticipant();
            fillParticipantProfile(person.getParticipantProfile(), personForm);
        }

        if (personForm.isActivist()) {
            person.makeActivist();
            fillActivistProfile(person.getActivistProfile(), personForm);
        }

        if (personForm.isReferent()) {
            person.makeReferent();
            fillReferentProfile(person.getReferentProfile(), personForm);
        }


        return person;
    }

    /**
     * @param child the child-to-be
     * @param parentForm the containing the parent data
     * @return the {@link Person} object encoded by the form
     */
    public Person extractParent(Person child, CreateParentForm parentForm) {
    	Person parent = personFactory.buildNew(parentForm.getFirstName(), parentForm.getLastName(), parentForm.getEmail()).makeParticipant().create();
    	parent.setPhoneNumber(parentForm.getPhoneNumber());

    	if (parentForm.isUseChildAddress()) {
    		parent.setAddress(child.getAddress());
    	} else {
    		parent.setAddress(new Address());
    	}

    	return parent;
    }

    /**
     * @param profile the profile to complete
     * @param personForm the data to use
     */
    public void fillParticipantProfile(ParticipantProfile profile, CreatePersonForm personForm) {
        Gender gender = Gender.valueOf(personForm.getGender());
        profile.setGender(gender);
        LocalDate dob = personForm.hasDateOfBirth() ? LocalDate.parse(personForm.getDateOfBirth(), dateFormatter) : null;
        profile.setDateOfBirth(dob);
        NabuMembership nabu = personForm.isNabuMember() ? new NabuMembership(personForm.getNabuNumber()) : new NabuMembership();
        profile.setNabuMembership(nabu);
        profile.setEatingHabits(personForm.getEatingHabit());
        profile.setHealthImpairments(personForm.getHealthImpairments());
        profile.setRemarks(personForm.getRemarks());
    }

    /**
     * @param profile the profile to complete
     * @param personForm the data to use
     */
    public void fillActivistProfile(ActivistProfile profile, CreatePersonForm personForm) {
        if (!personForm.getHasJuleica()) {
            return;
        }
        JuleicaCard juleicaCard = new JuleicaCard();
        extractJuleicaExpiryDate(personForm).ifPresent(juleicaCard::setExpiryDate);
        profile.setJuleicaCard(juleicaCard);
    }

    /**
     * @param profile the profile to complete
     * @param personForm the data to use
     */
    public void fillReferentProfile(ReferentProfile profile, CreatePersonForm personForm) {
        extractQualifications(personForm, qualificationRepo).ifPresent(qs -> qs.forEach(q -> profile.addQualification(q)));
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
