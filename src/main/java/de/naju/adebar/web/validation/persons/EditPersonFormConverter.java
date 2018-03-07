package de.naju.adebar.web.validation.persons;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import de.naju.adebar.model.Address;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.core.AddressForm;

/**
 * Simple service to apply the data from an {@link EditPersonForm} to {@link Person} objects.
 *
 * @author Rico Bergmann
 */
@Service
public class EditPersonFormConverter
    implements ValidatingEntityFormConverter<Person, EditPersonForm> {

  private final ValidatingEntityFormConverter<Address, AddressForm> addressFormConverter;

  /**
   * Full constructor
   *
   * @param addressFormConverter the converter to handle conversion of addresses
   */
  public EditPersonFormConverter(
      ValidatingEntityFormConverter<Address, AddressForm> addressFormConverter) {
    Assert.notNull(addressFormConverter, "The addressFormConverter may not be null");
    this.addressFormConverter = addressFormConverter;
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
        addressFormConverter.toForm(entity.getAddress()));
  }

}
