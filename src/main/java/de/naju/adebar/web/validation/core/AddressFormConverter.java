package de.naju.adebar.web.validation.core;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Simple service to convert {@link Address} instances to their corresponding {@link AddressForm}
 *
 * @author Rico Bergmann
 */
@Service
public class AddressFormConverter implements ValidatingEntityFormConverter<Address, AddressForm> {

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return AddressForm.class.equals(clazz);
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
      throw new IllegalArgumentException("Validation not supported for class " + target.getClass());
    }

    // if an AddressForm has no invalid configurations
  }

  @Override
  public boolean isValid(AddressForm form) {
    return form != null;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.validation.EntityFormConverter#toEntity(java.lang.Object)
   */
  @Override
  public Address toEntity(AddressForm form) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is not valid " + form);
    }
    return new Address(form.getStreet(), form.getZip(), form.getCity());
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.validation.EntityFormConverter#toForm(java.lang.Object)
   */
  @Override
  public AddressForm toForm(Address entity) {
    if (entity == null) {
      return null;
    }
    return new AddressForm(entity.getStreet(), entity.getZip(), entity.getCity());
  }

}
