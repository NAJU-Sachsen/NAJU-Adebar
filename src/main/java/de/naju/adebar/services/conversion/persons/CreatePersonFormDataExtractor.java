package de.naju.adebar.services.conversion.persons;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import de.naju.adebar.controller.forms.persons.CreateParentForm;
import de.naju.adebar.controller.forms.persons.CreatePersonForm;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.persons.Address;
import de.naju.adebar.model.persons.Gender;
import de.naju.adebar.model.persons.JuleicaCard;
import de.naju.adebar.model.persons.NabuMembershipInformation;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonFactory.ActivistBuilder;
import de.naju.adebar.model.persons.PersonFactory.ParticipantBuilder;
import de.naju.adebar.model.persons.PersonFactory.PersonBuilder;
import de.naju.adebar.model.persons.PersonFactory.ReferentBuilder;
import de.naju.adebar.model.persons.Qualification;
import de.naju.adebar.model.persons.QualificationRepository;

/**
 * Service to extract the necessary data from a 'create person' form
 *
 * @author Rico Bergmann
 * @see CreatePersonForm
 */
@Service
public class CreatePersonFormDataExtractor {

  private PersonFactory personFactory;
  private QualificationRepository qualificationRepo;
  private DateTimeFormatter dateFormatter;

  @Autowired
  public CreatePersonFormDataExtractor(PersonFactory personFactory,
      QualificationRepository qualificationRepo) {
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
    PersonBuilder builder = personFactory
        .buildNew(personForm.getFirstName(), personForm.getLastName(), personForm.getEmail()) //
        .specifyPhoneNumber(personForm.getPhoneNumber()) //
        .specifyAddress(extractAddress(personForm));

    if (personForm.isParticipant()) {
      ParticipantBuilder participantBuilder = builder.makeParticipant();
      fillParticipantProfile(participantBuilder, personForm);
      builder = participantBuilder.done();
    }

    if (personForm.isActivist()) {
      ActivistBuilder activistBuilder = builder.makeActivist();
      fillActivistProfile(activistBuilder, personForm);
      builder = activistBuilder.done();
    }

    if (personForm.isReferent()) {
      ReferentBuilder referentBuilder = builder.makeReferent();
      fillReferentProfile(referentBuilder, personForm);
      builder = referentBuilder.done();
    }

    return builder.create();
  }

  /**
   * @param child the child-to-be
   * @param parentForm the containing the parent data
   * @return the {@link Person} object encoded by the form
   */
  public Person extractParent(Person child, CreateParentForm parentForm) {
    PersonBuilder parentBuilder = personFactory.buildNew(parentForm.getFirstName(),
        parentForm.getLastName(), parentForm.getEmail());
    parentBuilder.specifyPhoneNumber(parentForm.getPhoneNumber());

    if (parentForm.isUseChildAddress()) {
      parentBuilder.specifyAddress(child.getAddress());
    }

    parentBuilder.makeParticipant().done();

    return parentBuilder.create();
  }

  /**
   * @param profile the profile to complete
   * @param personForm the data to use
   */
  public void fillParticipantProfile(ParticipantBuilder builder, CreatePersonForm personForm) {
    Gender gender = Gender.valueOf(personForm.getGender());
    builder.specifyGender(gender);

    if (personForm.hasDateOfBirth()) {
      builder.specifyDateOfBirth(LocalDate.parse(personForm.getDateOfBirth(), dateFormatter));
    }

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
    builder.specifyNabuMembership(nabuMembershipInformation);

    builder.specifyEatingHabits(personForm.getEatingHabit());
    builder.specifyHealthImpairments(personForm.getHealthImpairments());
    builder.addRemarks(personForm.getRemarks());
  }

  /**
   * @param profile the profile to complete
   * @param personForm the data to use
   */
  public void fillActivistProfile(ActivistBuilder builder, CreatePersonForm personForm) {
    extractJuleicaExpiryDate(personForm)
        .ifPresent(date -> builder.specifyJuleicaCard(new JuleicaCard(date)));
  }

  /**
   * @param profile the profile to complete
   * @param personForm the data to use
   */
  public void fillReferentProfile(ReferentBuilder builder, CreatePersonForm personForm) {
    extractQualifications(personForm, qualificationRepo).ifPresent(builder::specifyQualifications);
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
    if (!personForm.isActivist() //
        || !personForm.getHasJuleica() //
        || !personForm.hasJuleicaExpiryDate()) {
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
  public Optional<List<Qualification>> extractQualifications(CreatePersonForm personForm,
      QualificationRepository qualificationRepo) {
    if (!personForm.isReferent()) {
      return Optional.empty();
    } else {
      return Optional.of( //
          Lists.newArrayList( //
              qualificationRepo.findAll(personForm.getQualifications())));
    }
  }

  /**
   * @param personForm the form containing the data to extract
   * @param groupRepo repository containing all available local groups
   * @return an {@link Optional} containing all local groups the person is active in, or an empty
   *         optional if the person is no activist
   */
  public Optional<Iterable<LocalGroup>> extractActivistLocalGroups(CreatePersonForm personForm,
      ReadOnlyLocalGroupRepository groupRepo) {
    if (!personForm.isActivist()) {
      return Optional.empty();
    } else {
      return Optional.of(groupRepo.findAll(personForm.getLocalGroups()));
    }
  }
}
