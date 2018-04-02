package de.naju.adebar.web.validation.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.ParentProfile;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.util.Validation;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.core.AddressFormConverter;
import de.naju.adebar.web.validation.persons.participant.EditParticipantForm;
import de.naju.adebar.web.validation.persons.participant.EditParticipantFormConverter;
import de.naju.adebar.web.validation.persons.relatives.EditParentProfileForm;
import de.naju.adebar.web.validation.persons.relatives.EditParentProfileFormConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Simple service to apply the data from an {@link EditPersonForm} to {@link Person} objects.
 *
 * @author Rico Bergmann
 */
@Service
public class EditPersonFormConverter implements //
    ValidatingEntityFormConverter<Person, EditPersonForm> {

  private final AddressFormConverter addressFormConverter;
  private final EditParticipantFormConverter participantFormConverter;
  private final EditParentProfileFormConverter parentFormConverter;

  /**
   * Full constructor
   *
   * @param addressFormConverter the converter to handle conversion of addresses
   * @param participantFormConverter the converter to handle conversion of a {@link
   *     ParticipantProfile}
   * @param parentFormConverter the converter to handle conversion of a {@link ParentProfile}
   */
  public EditPersonFormConverter(AddressFormConverter addressFormConverter,
      EditParticipantFormConverter participantFormConverter,
      EditParentProfileFormConverter parentFormConverter) {
    Assert2.noNullArguments("No argument may be null", //
        addressFormConverter, participantFormConverter, parentFormConverter);

    this.addressFormConverter = addressFormConverter;
    this.participantFormConverter = participantFormConverter;
    this.parentFormConverter = parentFormConverter;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return EditPersonForm.class.isAssignableFrom(clazz);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation is not supported for instances of " + target.getClass());
    }

    // just validate step by step...

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required");

    EditPersonForm editPersonForm = (EditPersonForm) target;

    if (editPersonForm.hasEmail() && !Validation.isEmail(editPersonForm.getEmail())) {
      errors.rejectValue("email", "email.invalid");
    }

    if (editPersonForm.hasPhoneNUmber()
        && !Validation.isPhoneNumber(editPersonForm.getPhoneNumber())) {
      errors.rejectValue("phoneNumber", "phone.invalid");
    }

    try {
      errors.pushNestedPath("address");
      AddressForm addressForm = editPersonForm.getAddress();
      ValidationUtils.invokeValidator(this.addressFormConverter, addressForm, errors);
    } finally {
      errors.popNestedPath();
    }

    if (editPersonForm.hasParticipantForm()) {
      try {
        errors.pushNestedPath("participantForm");
        EditParticipantForm participantForm = editPersonForm.getParticipantForm();
        ValidationUtils.invokeValidator(this.participantFormConverter, participantForm, errors);
      } finally {
        errors.popNestedPath();
      }
    }

    if (editPersonForm.hasParentForm()) {
      try {
        errors.pushNestedPath("parentForm");
        EditParentProfileForm parentForm = editPersonForm.getParentForm();
        ValidationUtils.invokeValidator(this.parentFormConverter, parentForm, errors);
      } finally {
        errors.popNestedPath();
      }
    }

  }

  @Override
  public boolean isValid(EditPersonForm form) {
    // just validate step by step ...

    if (form.getFirstName() == null || form.getFirstName().isEmpty()) {
      return false;
    }

    if (form.getLastName() == null || form.getLastName().isEmpty()) {
      return false;
    }

    if (form.hasEmail() && !Validation.isEmail(form.getEmail())) {
      return false;
    }

    if (form.hasPhoneNUmber() && !Validation.isPhoneNumber(form.getPhoneNumber())) {
      return false;
    }

    if (form.hasParticipantForm() && !participantFormConverter.isValid(form.getParticipantForm())) {
      return false;
    }

    return !form.hasParentForm() || parentFormConverter.isValid(form.getParentForm());
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.validation.EntityFormConverter#toEntity(java.lang.Object)
   */
  @Override
  public Person toEntity(EditPersonForm form) {
    throw new UnsupportedOperationException(
        "An EditPersonForm may only be applied to existing persons");
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.validation.EntityFormConverter#applyFormToEntity(java.lang.Object,
   * java.lang.Object)
   */
  @Override
  public void applyFormToEntity(EditPersonForm form, Person entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid " + form);
    }

    Email email = form.hasEmail() ? Email.of(form.getEmail()) : null;
    PhoneNumber phone = form.hasPhoneNUmber() ? PhoneNumber.of(form.getPhoneNumber()) : null;

    entity.updateInformation( //
        form.getFirstName(), //
        form.getLastName(), //
        email, //
        phone);

    entity.updateAddress(addressFormConverter.toEntity(form.getAddress()));
    makeParticipantIfNecessary(entity, form.isParticipant());
    applyParticipantFormIfPossible(form, entity);
    applyParentFormIfPossible(form, entity);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.validation.EntityFormConverter#toForm(java.lang.Object)
   */
  @Override
  public EditPersonForm toForm(Person entity) {
    return new EditPersonForm( //
        entity.getFirstName(), //
        entity.getLastName(), //
        entity.getEmail(), //
        entity.getPhoneNumber(), //
        addressFormConverter.toForm(entity.getAddress()), //
        participantFormConverter.toForm(entity.getParticipantProfile()), //
        parentFormConverter.toForm(entity.getParentProfile()));
  }

  /**
   * Turns a person into a participant if it should (and is not a participant already). However this
   * will never turn a participant into a "non-participant"
   *
   * @param entity the person
   * @param participant whether the person should be a participant
   */
  private void makeParticipantIfNecessary(Person entity, boolean participant) {
    if (!entity.isParticipant() && participant) {
      entity.makeParticipant();
    }
  }

  /**
   * If the person is a participant and the form contains participation information this will apply
   * the data from it to the person.
   *
   * @param form the form
   * @param entity the person
   */
  private void applyParticipantFormIfPossible(EditPersonForm form, Person entity) {
    if (entity.isParticipant() && form.hasParticipantForm()) {
      participantFormConverter.applyFormToEntity(form.getParticipantForm(),
          entity.getParticipantProfile());
    }
  }

  /**
   * If the person is a parent and the form contains parent related information this will apply the
   * data from it to the person.
   *
   * @param form the form
   * @param entity the person
   */
  private void applyParentFormIfPossible(EditPersonForm form, Person entity) {
    if (entity.isParent() && form.hasParentForm()) {
      parentFormConverter.applyFormToEntity(form.getParentForm(), entity.getParentProfile());
    }
  }

}
