package de.naju.adebar.web.validation.persons.participant;

import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.web.validation.NewOrExistingEntityForm.SubmittedData;
import de.naju.adebar.web.validation.NewOrExistingValidatingEntityFormConverter;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Service to apply the data from an {@link SimplifiedAddParticipantForm} to {@link Person}
 * instances.
 *
 * @author Rico Bergmann
 */
@Service
public class SimplifiedAddParticipantFormConverter implements
    NewOrExistingValidatingEntityFormConverter<Person, SimplifiedAddParticipantForm> {

  private final PersonFactory personFactory;

  /**
   * Full constructor
   *
   * @param personFactory the factory used to create new person instances
   */
  public SimplifiedAddParticipantFormConverter(PersonFactory personFactory) {
    Assert.notNull(personFactory, "personFactory may not be null");
    this.personFactory = personFactory;
  }

  @Override
  public Person toExistingEntity(SimplifiedAddParticipantForm form) {
    return form.getExistingPerson();
  }

  @Override
  public Person toNewEntity(SimplifiedAddParticipantForm form) {
    if (!newEntityDataIsValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    //@formatter:off
    return personFactory.buildNew(
        form.getNewFirstName(),
        form.getNewLastName(),
        null)
        .makeParticipant()
        .specifyDateOfBirth(form.getNewDateOfBirth())
        .specifyGender(form.getNewGender()).create();
    //@formatter:on
  }

  @Override
  public boolean newEntityDataIsValid(SimplifiedAddParticipantForm form) {
    if (form.getFormStatus() != SubmittedData.NEW_ENTITY) {
      throw new IllegalStateException("Form should contain a new entity: " + form);
    }

    if (form.getNewFirstName() == null || form.getNewFirstName().isEmpty()) {
      return false;
    }

    if (form.getNewLastName() == null || form.getNewLastName().isEmpty()) {
      return false;
    }

    return genderIsSetForMinors(form);
  }

  @Override
  public boolean existingEntityDataIsValid(SimplifiedAddParticipantForm form) {
    if (form.getFormStatus() != SubmittedData.EXISTING_ENTITY) {
      throw new IllegalStateException("Form should contain an existing entity: " + form);
    }

    return true;
  }

  @Override
  public SimplifiedAddParticipantForm toForm(Person entity) {
    return new SimplifiedAddParticipantForm(entity);
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return SimplifiedAddParticipantForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for instances of " + target.getClass());
    }

    SimplifiedAddParticipantForm form = (SimplifiedAddParticipantForm) target;

    if (form.getFormStatus() == SubmittedData.EXISTING_ENTITY) {
      return;
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newFirstName", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newLastName", "field.required");

    if (!genderIsSetForMinors(form)) {
      errors.rejectValue("newGender", "participants.validation.underage.gender.required");
    }

  }

  /**
   * @param form the form to check
   * @return whether the person's gender is set if it is of minor age
   */
  private boolean genderIsSetForMinors(SimplifiedAddParticipantForm form) {
    if (form.getNewDateOfBirth() == null) {
      return true;
    }

    LocalDate now = LocalDate.now();
    Period timeDifference = Period.between(form.getNewDateOfBirth(), now);

    // if the person is of legal age, no gender is needed
    if (timeDifference.getYears() >= ParticipantProfile.LEGAL_AGE) {
      return true;
    }

    // otherwise the gender has to be set
    return form.getNewGender() != null;
  }
}
