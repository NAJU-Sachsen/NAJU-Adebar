package de.naju.adebar.web.validation.persons;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.core.AddressFormConverter;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Simple service to apply the data from an {@link EditPersonForm} to {@link Person} objects.
 *
 * @author Rico Bergmann
 */
@Service
public class EditPersonFormConverter
    implements ValidatingEntityFormConverter<Person, EditPersonForm> {

  private final AddressFormConverter addressFormConverter;
  private final EditParticipantFormConverter participantFormConverter;

  /**
   * Full constructor
   *
   * @param addressFormConverter the converter to handle conversion of addresses
   * @param participantFormConverter the converter to handle conversion of a {@link
   *     de.naju.adebar.model.persons.ParticipantProfile}
   */
  public EditPersonFormConverter(AddressFormConverter addressFormConverter,
      EditParticipantFormConverter participantFormConverter) {
    Assert2.noNullArguments( //
        "No argument may be null", addressFormConverter, participantFormConverter);

    this.addressFormConverter = addressFormConverter;
    this.participantFormConverter = participantFormConverter;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return EditPersonFormConverter.class.equals(clazz);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation is not supported for instances of " + target.getClass());
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required");

    AddressForm addressForm = ((EditPersonForm) target).getAddress();
    addressFormConverter.validate(addressForm, errors);
  }

  @Override
  public boolean isValid(EditPersonForm form) {
    return (form.getFirstName() != null && !form.getFirstName().isEmpty()) //
        && (form.getLastName() != null && !form.getLastName().isEmpty());
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
    entity.updateInformation( //
        form.getFirstName(), //
        form.getLastName(), //
        form.getEmail(), //
        form.getPhoneNumber());

    entity.updateAddress(addressFormConverter.toEntity(form.getAddress()));
    makeParticipantIfNecessary(entity, form.isParticipant());
    applyParticipantFormIfPossible(form, entity);
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
        participantFormConverter.toForm(entity.getParticipantProfile()));
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
      participantFormConverter
          .applyFormToEntity(form.getParticipantForm(), entity.getParticipantProfile());
    }
  }

}
