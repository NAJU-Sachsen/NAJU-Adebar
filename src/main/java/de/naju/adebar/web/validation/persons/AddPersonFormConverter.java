package de.naju.adebar.web.validation.persons;

import com.google.common.collect.Lists;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonFactory.PersonBuilder;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.core.AddressFormConverter;
import de.naju.adebar.web.validation.persons.activist.AddActivistForm;
import de.naju.adebar.web.validation.persons.activist.AddActivistFormConverter;
import de.naju.adebar.web.validation.persons.participant.AddParticipantForm;
import de.naju.adebar.web.validation.persons.participant.AddParticipantFormConverter;
import de.naju.adebar.web.validation.persons.referent.AddReferentForm;
import de.naju.adebar.web.validation.persons.referent.AddReferentFormConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Simple service to apply the data from an {@link AddPersonForm} to {@link Person} instances
 */
@Service
public class AddPersonFormConverter implements
    ValidatingEntityFormConverter<Person, AddPersonForm> {

  private final AddressFormConverter addressFormConverter;
  private final AddActivistFormConverter activistFormConverter;
  private final AddParticipantFormConverter participantFormConverter;
  private final AddReferentFormConverter referentFormConverter;
  private final PersonFactory personFactory;

  /**
   * Full constructor.
   *
   * @param addressFormConverter converter for the {@link AddressForm}
   * @param activistFormConverter converter for the {@link AddActivistForm}
   * @param participantFormConverter converter for the {@link AddParticipantForm}
   * @param referentFormConverter converter for the {@link AddReferentForm}
   * @param personFactory service to create the {@link Person} instances
   */
  public AddPersonFormConverter(
      AddressFormConverter addressFormConverter,
      AddActivistFormConverter activistFormConverter,
      AddParticipantFormConverter participantFormConverter,
      AddReferentFormConverter referentFormConverter,
      PersonFactory personFactory) {
    this.addressFormConverter = addressFormConverter;
    this.activistFormConverter = activistFormConverter;
    this.participantFormConverter = participantFormConverter;
    this.referentFormConverter = referentFormConverter;
    this.personFactory = personFactory;
  }

  @Override
  public boolean isValid(AddPersonForm form) {
    if (form == null) {
      return false;
    }

    if (form.getFirstName() == null || form.getFirstName().isEmpty()) {
      return false;
    } else if (form.getLastName() == null || form.getLastName().isEmpty()) {
      return false;
    }

    return addressFormIsValid(form) //
        && activistFormIsValid(form) //
        && participantFormIsValid(form) //
        && referentFormIsValid(form);
  }

  @Override
  public Person toEntity(AddPersonForm form) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    PersonBuilder builder = personFactory
        .buildNew(form.getFirstName(), form.getLastName(), form.getEmail());
    builder.specifyPhoneNumber(form.getPhone());
    builder.specifyAddress(addressFormConverter.toEntity(form.getAddress()));

    Person person = builder.create();

    if (form.isParticipant()) {
      person.makeParticipant();
      participantFormConverter.applyFormToEntity( //
          form.getParticipantForm(), person.getParticipantProfile());
    }

    if (form.isActivist()) {
      person.makeActivist();
      activistFormConverter.applyFormToEntity(form.getActivistForm(), person.getActivistProfile());
    }

    if (form.isReferent()) {
      person.makeReferent();
      referentFormConverter.applyFormToEntity(form.getReferentForm(), person.getReferentProfile());
    }

    return person;
  }

  @Override
  public AddPersonForm toForm(Person entity) {
    AddPersonForm form = new AddPersonForm( //
        entity.getFirstName(), //
        entity.getLastName(), //
        entity.getEmail(), //
        entity.getPhoneNumber(), //
        addressFormConverter.toForm(entity.getAddress()));
    form.setParticipantForm(participantFormConverter.toForm( //
        entity.getParticipantProfile(), Lists.newArrayList(entity.getParticipatingEvents())));
    form.setActivistForm(activistFormConverter.toForm(entity.getActivistProfile()));
    form.setReferentForm(referentFormConverter.toForm(entity.getReferentProfile()));
    return form;
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return AddPersonForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for instances of " + target.getClass());
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required");

    AddPersonForm form = (AddPersonForm) target;
    validateAddressForm(form, errors);
    validateActivistForm(form, errors);
    validateParticipantForm(form, errors);
    validateReferentForm(form, errors);
  }

  /**
   * Validates the {@link AddressForm} inside an {@link AddPersonForm}
   *
   * @param form the form
   * @param errors the errors
   */
  private void validateAddressForm(AddPersonForm form, Errors errors) {
    try {
      errors.pushNestedPath("address");
      ValidationUtils.invokeValidator(addressFormConverter, form.getAddress(), errors);
    } finally {
      errors.popNestedPath();
    }
  }

  /**
   * Validates the {@link AddActivistForm} inside an {@link AddPersonForm}
   *
   * @param form the form
   * @param errors the errors
   */
  private void validateActivistForm(AddPersonForm form, Errors errors) {
    try {
      errors.pushNestedPath("activist");
      ValidationUtils.invokeValidator(activistFormConverter, form.getActivistForm(), errors);
    } finally {
      errors.popNestedPath();
    }
  }

  /**
   * Validates the {@link AddParticipantForm} inside an {@link AddPersonForm}
   *
   * @param form the form
   * @param errors the errors
   */
  private void validateParticipantForm(AddPersonForm form, Errors errors) {
    try {
      errors.pushNestedPath("participant");
      ValidationUtils.invokeValidator(participantFormConverter, form.getParticipantForm(), errors);
    } finally {
      errors.popNestedPath();
    }
  }

  /**
   * Validates the {@link AddReferentForm} inside an {@link AddPersonForm}
   *
   * @param form the form
   * @param errors the errors
   */
  private void validateReferentForm(AddPersonForm form, Errors errors) {
    try {
      errors.pushNestedPath("referent");
      ValidationUtils.invokeValidator(referentFormConverter, form.getReferentForm(), errors);
    } finally {
      errors.popNestedPath();
    }
  }

  /**
   * @param form the form to check
   * @return whether the {@link AddressForm} inside the form is valid
   */
  private boolean addressFormIsValid(AddPersonForm form) {
    return form.getAddress() == null || addressFormConverter.isValid(form.getAddress());
  }

  /**
   * @param form the form to check
   * @return whether the {@link AddActivistForm} inside the form is valid
   */
  private boolean activistFormIsValid(AddPersonForm form) {
    return form.getActivistForm() == null || activistFormConverter.isValid(form.getActivistForm());
  }

  /**
   * @param form the form to check
   * @return whether the {@link AddParticipantForm} inside the form is valid
   */
  private boolean participantFormIsValid(AddPersonForm form) {
    return form.getParticipantForm() == null || participantFormConverter
        .isValid(form.getParticipantForm());
  }

  /**
   * @param form the form to check
   * @return whether the {@link AddReferentForm} inside the form is valid
   */
  private boolean referentFormIsValid(AddPersonForm form) {
    return form.getReferentForm() == null || referentFormConverter.isValid(form.getReferentForm());
  }

}
