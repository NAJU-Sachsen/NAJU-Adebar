package de.naju.adebar.web.validation.persons.participant;

import de.naju.adebar.documentation.DesignSmell;
import de.naju.adebar.documentation.ddd.BusinessRule;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.details.NabuMembershipInformation;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import java.time.LocalDate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Simple service to apply the data from an {@link EditParticipantForm} to {@link
 * ParticipantProfile} objects.
 *
 * @author Rico Bergmann
 */
@Service
public class EditParticipantFormConverter implements
    ValidatingEntityFormConverter<ParticipantProfile, EditParticipantForm> {

  @Override
  public ParticipantProfile toEntity(EditParticipantForm form) {
    throw new UnsupportedOperationException("An EditParticipantForm may only be applied");
  }

  @Override
  public EditParticipantForm toForm(ParticipantProfile entity) {
    if (entity == null) {
      return null;
    }

    return new EditParticipantForm( //
        entity.getGender(), //
        entity.getDateOfBirth(), //
        entity.getEatingHabits(), //
        entity.getHealthImpairments(), //
        entity.getNabuMembership(), //
        entity.getRemarks());
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return EditParticipantForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation is not supported for instances of " + target.getClass());
    }

    EditParticipantForm form = (EditParticipantForm) target;
    performValidation(form, errors);
  }

  @Override
  public boolean isValid(EditParticipantForm form) {
    return eatingHabitsContentLengthIsWithinRange(form) //
        && healthImpairmentsContentLengthIsWithinRange(form) //
        && remarksContentLengthIsWithinRange(form);
  }

  @Override
  public void applyFormToEntity(EditParticipantForm form, ParticipantProfile entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid " + form);
    }

    entity.updateProfile(form.getGender(), form.getDateOfBirth(), form.getEatingHabits(),
        form.getHealthImpairments());
    entity.updateRemarks(form.getRemarks());

    switch (form.getNabuMember()) {
      case UNKNOWN:
        entity.updateNabuMembership(null);
        break;
      case IS_MEMBER:
        entity.updateNabuMembership(new NabuMembershipInformation(form.getNabuMembershipNumber()));
        break;
      case NO_MEMBER:
        entity.updateNabuMembership(new NabuMembershipInformation(false));
        break;
    }
  }

  /**
   * Checks the form for errors
   *
   * @param form the form to check
   * @param errors object to keep track of occurring errors.
   */
  private void performValidation(EditParticipantForm form, Errors errors) {
    if (!dateOfBirthIsPast(form)) {
      errors.rejectValue("dateOfBirth", "field.invalid");
    }

    if (!genderIsSetForMinors(form)) {
      errors.rejectValue("gender", "participants.validation.underage.gender.required");
    }

    if (!eatingHabitsContentLengthIsWithinRange(form)) {
      errors.rejectValue("eatingHabits", "field.too-long");
    }

    if (!healthImpairmentsContentLengthIsWithinRange(form)) {
      errors.rejectValue("healthImpairments", "field.too-long");
    }

    if (!remarksContentLengthIsWithinRange(form)) {
      errors.rejectValue("remarks", "field.too-long");
    }
  }

  /**
   * @param form the form to analyse
   * @return whether the date of birth lies in the past. This is required for a date of birth to be
   *     valid.
   */
  @BusinessRule
  private boolean dateOfBirthIsPast(EditParticipantForm form) {
    if (form.getDateOfBirth() == null) {
      return true;
    }

    LocalDate now = LocalDate.now();
    return !now.isBefore(form.getDateOfBirth());

  }

  /**
   * @param form the form to analyse
   * @return whether the gender is set if necessary
   */
  @BusinessRule
  @DesignSmell(description = "The converter should not be responsible for expressing business rules",
      reason = "The participant profile has nothing to do with a web form and should therefore not "
          + "be responsible for validating it. Furthermore the converter needs to reject illegal"
          + "values and therefore has to do the validation")
  private boolean genderIsSetForMinors(EditParticipantForm form) {
    if (form.getDateOfBirth() == null) {
      return true;
    }

    // if the person is of legal age, no gender is needed
    if (Age.forDateOfBirth(form.getDateOfBirth()).isOfLegalAge()) {
      return true;
    }

    // otherwise the gender has to be set
    return form.getGender() != null;
  }

  /**
   * @param form the form to check
   * @return whether the eating habits are within the allowed range
   */
  private boolean eatingHabitsContentLengthIsWithinRange(EditParticipantForm form) {
    return form.getEatingHabits() == null //
        || form.getEatingHabits().length() <= EditParticipantForm.MAX_STRING_LENGTH;
  }

  /**
   * @param form the form to check
   * @return whether the health impairments are within the allowed range
   */
  private boolean healthImpairmentsContentLengthIsWithinRange(EditParticipantForm form) {
    return form.getHealthImpairments() == null //
        || form.getHealthImpairments().length() <= EditParticipantForm.MAX_STRING_LENGTH;
  }

  /**
   * @param form the form to check
   * @return whether the remarks are within the allowed range
   */
  private boolean remarksContentLengthIsWithinRange(EditParticipantForm form) {
    return form.getRemarks() == null //
        || form.getRemarks().length() <= EditParticipantForm.MAX_STRING_LENGTH;
  }
}
